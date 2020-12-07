package com.funcoders.Instadpsaver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.funcoders.Instadpsaver.bean.InstaBean;
import com.funcoders.Instadpsaver.common.Constants;
import com.funcoders.Instadpsaver.fragment.HistoryFragment;
import com.funcoders.Instadpsaver.fragment.SaveListFragment;
import com.funcoders.Instadpsaver.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity  {


    public static String TAG="MainActivity";
    ImageView option_menu,Img_back;
    TextView txt_appbartitle,txt_search,txt_history,txt_save;
    LinearLayout Logout, Share, Help;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    InstaBean instaBean=new InstaBean();
    Animation animFadein;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));
*/


       // presenter=new InstaDataPresenter(MainActivity.this,MainActivity.this);

      //    checkPermission(MainActivity.this);


        option_menu=findViewById(R.id.option_menu);
        txt_appbartitle=findViewById(R.id.txt_appbartitle);
        txt_search=findViewById(R.id.txt_search);
        txt_history=findViewById(R.id.txt_history);
        txt_save=findViewById(R.id.txt_save);
        Img_back=findViewById(R.id.imgBack);
       // progressBar = (ProgressBar)findViewById(R.id.spin_kit);
       // Sprite doubleBounce = new Circle();
      //  progressBar.setIndeterminateDrawable(doubleBounce);
      //  progressBar.setBackgroundColor(getResources().getColor(R.color.secondaryDarkColor));
       /* progressBar.setOutlineAmbientShadowColor(getResources().getColor(R.color.colorPrimaryDark));
        progressBar.setOutlineSpotShadowColor(getResources().getColor(R.color.colorAccent));
*/

       openSearchfragment();

       Img_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

       txt_history.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               txt_history.setAlpha(0.9f);
               txt_save.setAlpha(0.4f);
               txt_search.setAlpha(0.4f);
               openHistoryFragment();

           }
       });

       txt_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               txt_history.setAlpha(0.4f);
               txt_save.setAlpha(0.9f);
               txt_search.setAlpha(0.4f);
               openSaveListFragment();
           }
       });

       txt_search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               txt_history.setAlpha(0.4f);
               txt_save.setAlpha(0.4f);
               txt_search.setAlpha(0.9f);
               openSearchfragment();
           }
       });

        option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txt_appbartitle.setText("Profile Saver");

        option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.optionmenuwindow, null);
                final PopupWindow window = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setOutsideTouchable(true);
                window.showAtLocation(layout, Gravity.TOP | Gravity.END, 0, 90);
                Logout = layout.findViewById(R.id.downlod);
                Share = layout.findViewById(R.id.share);
                Help = layout.findViewById(R.id.help_ll);

                Share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareApp(MainActivity.this);
                    }
                });

                Logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(MainActivity.this,LoginActivity.class));

                        finish();
                    }
                });




            }
        });


    }

    public static void ShareApp(Context context) {
        final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message) + appLink);
        sendInt.setType("text/plain");
        context.startActivity(Intent.createChooser(sendInt, "Share"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }

    public void openSearchfragment()
    {
        Fragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
       /* bundle.putInt(Constants.EXTRA_COME_FROM, 1);
        bundle.putString(Constants.EXTRA_CUSTOMER_NO, sharedPreferences.getString(Constants.KEY_CUSTOMER_NO, ""));
        bundle.putString(Constants.EXTRA_CUSTOMER_NAME, sharedPreferences.getString(Constants.KEY_CUSTOMER_NAME, ""));
        bundle.putBoolean(Constants.EXTRA_READ_FROM_TECH_CACHE, readFromTechCache);
        bundle.putBoolean(Constants.LoginFlag, true);
        bundle.putBoolean("isFromRegistration", isFromRegistration);
//        if (fromLogin != null) {
//            bundle.putString("coimgFromLogin",fromLogin);
//        }
        mainMenuFragment.setArguments(bundle);*/
        openFragment(searchFragment);

    }


    public void openHistoryFragment()
    {
        Fragment historyFragment = new HistoryFragment();
        Bundle bundle = new Bundle();
       /* bundle.putInt(Constants.EXTRA_COME_FROM, 1);
        bundle.putString(Constants.EXTRA_CUSTOMER_NO, sharedPreferences.getString(Constants.KEY_CUSTOMER_NO, ""));
        bundle.putString(Constants.EXTRA_CUSTOMER_NAME, sharedPreferences.getString(Constants.KEY_CUSTOMER_NAME, ""));
        bundle.putBoolean(Constants.EXTRA_READ_FROM_TECH_CACHE, readFromTechCache);
        bundle.putBoolean(Constants.LoginFlag, true);
        bundle.putBoolean("isFromRegistration", isFromRegistration);
//        if (fromLogin != null) {
//            bundle.putString("coimgFromLogin",fromLogin);
//        }
        mainMenuFragment.setArguments(bundle);*/
        openFragment(historyFragment);

    } public void openSaveListFragment()
    {
        Fragment saveListFragment = new SaveListFragment();
        Bundle bundle = new Bundle();
       /* bundle.putInt(Constants.EXTRA_COME_FROM, 1);
        bundle.putString(Constants.EXTRA_CUSTOMER_NO, sharedPreferences.getString(Constants.KEY_CUSTOMER_NO, ""));
        bundle.putString(Constants.EXTRA_CUSTOMER_NAME, sharedPreferences.getString(Constants.KEY_CUSTOMER_NAME, ""));
        bundle.putBoolean(Constants.EXTRA_READ_FROM_TECH_CACHE, readFromTechCache);
        bundle.putBoolean(Constants.LoginFlag, true);
        bundle.putBoolean("isFromRegistration", isFromRegistration);
//        if (fromLogin != null) {
//            bundle.putString("coimgFromLogin",fromLogin);
//        }
        mainMenuFragment.setArguments(bundle);*/
        openFragment(saveListFragment);

    }




    public static void checkPermission(Activity activity) {
        // Check if we have write permission
        int storage = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int contacts = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
       // int camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (storage != PackageManager.PERMISSION_GRANTED || contacts != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    Constants.PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }



    public void openFragment(Fragment mainMenuFragment) {
        try {
            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, mainMenuFragment, mainMenuFragment.getClass().getName());
                //        fragmentTransaction.commit();
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }




}