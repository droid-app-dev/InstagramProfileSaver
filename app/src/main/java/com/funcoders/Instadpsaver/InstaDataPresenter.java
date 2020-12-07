package com.funcoders.Instadpsaver;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.funcoders.Instadpsaver.RoomDb.TaskAppDatabase;
import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.bean.InstaBean;
import com.funcoders.Instadpsaver.common.AppController;
import com.funcoders.Instadpsaver.common.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class InstaDataPresenter {

    MainMenuview menuview;
    Context mcontext;
    ArrayList<InstaBean> imagelist = new ArrayList<>();
    ArrayList<InstaBean> vediolist = new ArrayList<>();
    private TaskAppDatabase appDatabase;
    private Boolean isExist = false;
    private Object ContactModel;

    public InstaDataPresenter(MainMenuview activity, Context context) {
        mcontext = context;
        menuview = activity;
    }


    public void getInstaData(String id) {

        final InstaBean bean = new InstaBean();

        bean.setInsta_id(id);

        ((Activity) mcontext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menuview != null) {
                    menuview.showProgressDialog();
                }

            }
        });

        /*InstaProfile.Companion.getInfo(mcontext, id, new InstaProfileListener() {
            @Override
            public void onSuccess(String full_name, Integer followers, Integer following, String profile_pic_url_hd, String biography,
                                  String external_url, Boolean is_private, Boolean is_verified, Integer total_media_timeline, Integer total_video_timeline, Integer highlight_count) {

                bean.setFull_name(full_name);
                bean.setFollowers(followers);
                bean.setFollowing(following);
                bean.setProfile_pic_url_hd(profile_pic_url_hd);
                bean.setBiography(biography);
                bean.setExternal_url(external_url);
                bean.setIs_private(is_private);
                bean.setIs_verified(is_verified);
                bean.setTotal_media_timeline(total_media_timeline);
                bean.setTotal_video_timeline(total_video_timeline);
                bean.setHighlight_count(highlight_count);

                ((Activity)mcontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(menuview!=null){
                            menuview.hideProgressDialog();
                            menuview.displayIntaData(bean);
                        }
                    }
                });

            }

            @Override
            public void onError(String s) {
                ((Activity)mcontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(menuview!=null){
                            menuview.hideProgressDialog();
                        }
                        Constants.displayLongToast(mcontext,"Invalid UserName");
                    }
                });

            }
        });
*/

      //  List<ContactModel> contactModelList = getContacts(mcontext);

      //  System.out.println("Contactlist Size "+contactModelList.size());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "https://www.instagram.com/" + id + "/?__a=1", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.d(TAG, response.toString());

                try {
                    JSONObject obj = response.getJSONObject("graphql").getJSONObject("user");
                    bean.setFull_name(obj.getString("full_name"));
                    bean.setFollowers(obj.getJSONObject("edge_followed_by").getInt("count"));
                    bean.setFollowing(obj.getJSONObject("edge_follow").getInt("count"));
                    bean.setProfile_pic_url_hd(obj.getString("profile_pic_url_hd"));
                    bean.setBiography(obj.getString("biography"));
                    bean.setExternal_url(obj.getString("external_url"));
                    bean.setIs_private(obj.getBoolean("is_private"));
                    bean.setIs_verified(obj.getBoolean("is_verified"));
                    bean.setTotal_video_timeline(obj.getJSONObject("edge_felix_video_timeline").getInt("count"));
                    bean.setHighlight_count(obj.getInt("highlight_reel_count"));
                    bean.setTotal_media_timeline(obj.getJSONObject("edge_owner_to_timeline_media").getInt("count"));

                    JSONArray jsonArray = obj.getJSONObject("edge_owner_to_timeline_media").getJSONArray("edges");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        InstaBean imagebean = new InstaBean();
                        JSONObject json = jsonArray.getJSONObject(i).getJSONObject("node");
                        imagebean.setDisplay_url(json.getString("display_url"));

                        if (json.has("video_url")) {
                            imagebean.setVideo_url(json.getString("video_url"));
                            vediolist.add(imagebean);
                        }

                        imagelist.add(imagebean);


                    }

                    System.out.println("arraylength" + bean.getDisplay_url());


                    System.out.println("arraylength" + jsonArray.length());

                    ((Activity) mcontext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (menuview != null) {
                                menuview.hideProgressDialog();
                                menuview.displayIntaData(bean);
                                menuview.displayImages(imagelist);
                                menuview.displayVedio(vediolist);
                            }
                        }
                    });

                } catch (final JSONException e) {
                    e.printStackTrace();
                    ((Activity) mcontext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (menuview != null) {
                                menuview.hideProgressDialog();
                            }
                            Constants.displayLongToast(mcontext, "" + e.getMessage());
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ((Activity) mcontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (menuview != null) {
                            menuview.hideProgressDialog();
                        }
                        Constants.displayLongToast(mcontext, "Sorry, user not found. Please make sure the username is correct and try again.");
                    }
                });
            }
        });

        // Adding request to request queue
        AppController.getInstance(mcontext).addToRequestQueue(jsonObjReq);
    }


    public void saveImage(String url, String id) throws IOException {

        ((Activity) mcontext).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Constants.displayLongToast(mcontext, "Downloading..!");
            }
        });
        saveImag(url, id);

    }

   /* private void saveToGallery(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/MyPics");
        dir.mkdirs();

        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public void saveImag(String imageUrl, String filename) throws IOException {

        System.out.println("Image url "+imageUrl.toString());

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/InstaProfileDownload");

        if (!direct.exists()) {
            direct.mkdirs();
        }



        try{
            DownloadManager dm = (DownloadManager) mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(imageUrl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(mcontext, "Image download started.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(mcontext, "Image download failed.", Toast.LENGTH_SHORT).show();
        }

       /* DownloadManager mgr = (DownloadManager) mcontext.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(imageUrl);

        System.out.println("downloadUri "+downloadUri.toString());

        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/InstaSaveProfiles", id + ".jpg");

        mgr.enqueue(request);

        // Open Download Manager to view File progress
        //  Toast.makeText(mcontext, "Downloading...",Toast.LENGTH_LONG).show();
        // startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
*/

    }


    private void downloadImageNew(String filename, String downloadUrlOfImage){
        try{
            DownloadManager dm = (DownloadManager) mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(mcontext, "Image download started.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(mcontext, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }



}
