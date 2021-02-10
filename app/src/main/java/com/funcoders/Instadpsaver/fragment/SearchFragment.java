package com.funcoders.Instadpsaver.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.funcoders.Instadpsaver.BuildConfig;
import com.funcoders.Instadpsaver.InstaDataPresenter;
import com.funcoders.Instadpsaver.MainActivity;
import com.funcoders.Instadpsaver.MainMenuview;
import com.funcoders.Instadpsaver.R;
import com.funcoders.Instadpsaver.RoomDb.TaskAppDatabase;
import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.bean.InstaBean;
import com.funcoders.Instadpsaver.bean.InstaSearchHistorybean;
import com.funcoders.Instadpsaver.bean.ProfileBean;
import com.funcoders.Instadpsaver.common.AddUtils;
import com.funcoders.Instadpsaver.common.Constants;
import com.funcoders.Instadpsaver.contactsevice.ContactService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements MainMenuview {


    private static final int STORAGE_PERMISSION_CODE_Storage = 10;
    private static final int STORAGE_PERMISSION_CODE_Contact = 100;
    EditText Insta_username;
    ImageView next_button, insta_profile, search_clear;
    ImageView downlod;
    String version="";
    public static String TAG = "MainActivity";
    ImageView option_menu, img_save;
    TextView txt_appbartitle;
    LinearLayout ll_downlod, ll_save;
    InstaDataPresenter presenter = null;
    ProgressDialog progressDialog = null;
    private TaskAppDatabase appDatabase;
    private Boolean isExist = false;
    Boolean savebtnclicked = false;
    InstaBean instaBean = new InstaBean();
    String Insta_ID;
    SharedPreferences sharedPreferences;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Insta_ID = bundle.getString(Constants.EXTRA_HISTORY_InstaID);

        }
        AdView mAdView = (AdView) getView().findViewById(R.id.adView);
        AddUtils.showGoogleBannerAd(getActivity(),mAdView);
        InterstitialAdsINIT();

      //version= getAppversion();



            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            checkPermission(Manifest.permission.READ_CONTACTS, STORAGE_PERMISSION_CODE_Contact);
        } else {
            sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
          //  if (sharedPreferences.getBoolean(Constants.isContactsPosted, true)) {
                System.out.println("Service Triggred.....1");
                getActivity().startService(new Intent(getActivity(), ContactService.class));
           // }
        }


        presenter = new InstaDataPresenter(this, getActivity());


        appDatabase = TaskAppDatabase.getInstance(getActivity());
        Insta_username = view.findViewById(R.id.insta_username);
        next_button = view.findViewById(R.id.imag_next);
        search_clear = view.findViewById(R.id.search_clear);
        insta_profile = view.findViewById(R.id.insta_profile);
        downlod = view.findViewById(R.id.downlod);
        option_menu = view.findViewById(R.id.option_menu);
        txt_appbartitle = view.findViewById(R.id.txt_appbartitle);
        ll_downlod = view.findViewById(R.id.ll_downlod);
        ll_save = view.findViewById(R.id.ll_save);
        img_save = view.findViewById(R.id.save);
        ll_downlod.setVisibility(View.GONE);
        ll_save.setVisibility(View.GONE);

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savebtnclicked) {
                    savebtnclicked = false;
                    img_save.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.save_empty));
                    removefromdb(instaBean);
                } else {
                    savebtnclicked = true;
                    img_save.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.savefill_color));
                    savetodb(instaBean);
                }

            }


        });

        if (Insta_ID != null && !Insta_ID.equalsIgnoreCase("")) {
            Insta_username.setText(Insta_ID);
        }

        Insta_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0) {
                    search_clear.setColorFilter(getResources().getColor(R.color.grey));
                } else {
                    search_clear.setColorFilter(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insta_username.setText("");
            }
        });
        insta_profile.setVisibility(View.GONE);
        downlod.setVisibility(View.GONE);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(Insta_username.getText().toString())) {


                    insta_profile.setVisibility(View.GONE);
                    downlod.setVisibility(View.GONE);
                    ll_downlod.setVisibility(View.GONE);
                    ll_save.setVisibility(View.GONE);
                    if (Constants.isNetworkAvailable(getActivity())) {
                        presenter.getInstaData(Insta_username.getText().toString());
                    } else {
                        Constants.displayLongToast(getActivity(), "Can't connect to the internet Please check your connection and try again.");
                    }

                } else {

                    Insta_username.setError("user name can't empty");

                }

            }
        });

        downlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE_Storage);
                } else {
                    startdownload();
                }

            }
        });

    }


    @Override
    public void showProgressDialog() {
        progressDialog = Constants.showProgressDialog(getActivity(), "");

    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void displayIntaData(final InstaBean bean) {

        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bean != null) {
                    instaBean = bean;
                    InstaSearchHistorybean searchbean = new InstaSearchHistorybean();
                    searchbean.setInsta_id(bean.getInsta_id());
                    appDatabase.taskDao().insertids(searchbean);
                    if (checkProfileisExist(bean.getInsta_id())) {
                        savebtnclicked = true;
                        img_save.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.savefill_color));
                        ProfileBean profileBean = rowdata(instaBean);
                        appDatabase.taskDao().update(profileBean);
                    } else {
                        savebtnclicked = false;
                        img_save.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.save_empty));
                    }
                    insta_profile.setVisibility(View.VISIBLE);
                    downlod.setVisibility(View.VISIBLE);
                    ll_downlod.setVisibility(View.VISIBLE);
                    ll_save.setVisibility(View.VISIBLE);

                    Glide.with(getActivity())
                            .load(bean.getProfile_pic_url_hd())
                            .placeholder(R.drawable.progress_animation)
                            // .error(R.drawable.ic_arrow_back_24_px)
                            .into(insta_profile);


                } else {
                    ll_downlod.setVisibility(View.GONE);
                    ll_save.setVisibility(View.GONE);


                }
            }
        });

    }


    public boolean checkProfileisExist(String instaid) {
        Boolean isExist = false;
        List<ProfileBean> allidslist = appDatabase.taskDao().getAllids();
        for (int i = 0; i < allidslist.size(); i++) {
            if (allidslist.get(i).getInsta_id().equalsIgnoreCase(instaid)) {
                isExist = true;
                break;
            } else {
                isExist = false;
            }
        }
        return isExist;
    }

    private void savetodb(InstaBean instaBean) {
        if (instaBean != null) {
            ProfileBean profileBean = rowdata(instaBean);
            List<ProfileBean> allidslist = appDatabase.taskDao().getAllids();

            for (int i = 0; i < allidslist.size(); i++) {
                if (allidslist.get(i).getInsta_id().equalsIgnoreCase(instaBean.getInsta_id())) {
                    isExist = false;
                    break;
                } else {
                    isExist = true;
                }
            }

            if (!isExist) {
                appDatabase.taskDao().update(profileBean);
                System.out.println("call update");
            } else {
                System.out.println("call insert");
                appDatabase.taskDao().insert(profileBean);
            }

            if (allidslist.size() == 0) {
                System.out.println("call insert");
                appDatabase.taskDao().insert(profileBean);

            }
        }


    }


    public ProfileBean rowdata(InstaBean instaBean) {
        ProfileBean profileBean = new ProfileBean();
        profileBean.setInsta_id(instaBean.getInsta_id());
        profileBean.setFollowers(instaBean.getFollowers());
        profileBean.setFollowing(instaBean.getFollowing());
        profileBean.setBiography(instaBean.getBiography());
        profileBean.setFull_name(instaBean.getFull_name());
        if (instaBean.getIs_private()) {
            profileBean.setIs_private("true");
        } else {
            profileBean.setIs_private("false");

        }
        profileBean.setProfile_pic_url_hd(instaBean.getProfile_pic_url_hd());

        return profileBean;

    }

    private void removefromdb(InstaBean instaBean) {

        ProfileBean profileBean = rowdata(instaBean);

        if (profileBean != null) {
            appDatabase.taskDao().delete(profileBean);
        }

    }

    @Override
    public void displayImages(ArrayList<InstaBean> imgList) {

    }

    @Override
    public void displayVedio(ArrayList<InstaBean> vedioList) {

    }


    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        System.out.println("Request Permisson Triggred...1" + requestCode + "" + permissions.toString() + "" + grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE_Storage) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startdownload();
            } else {
                Toast.makeText(getActivity(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == STORAGE_PERMISSION_CODE_Contact) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                if (sharedPreferences.getBoolean(Constants.isContactsPosted, true)) {
                    System.out.println("Service Triggred.....1");
                    getActivity().startService(new Intent(getActivity(), ContactService.class));
                }
            } else {
                Toast.makeText(getActivity(), "Contact permission Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void startdownload() {

        if (Constants.isNetworkAvailable(getActivity())) {
            try {
                presenter.saveImage(instaBean.getProfile_pic_url_hd(), instaBean.getInsta_id());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Constants.displayLongToast(getActivity(), "Can't connect to the internet Please check your connection and try again.");

        }

        showInterstitial();

    }

    public boolean checkPermission() {
        int contacts = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
        if (contacts == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;

        }
    }



    public void InterstitialAdsINIT(){

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    private String getAppversion()
    {

        /*mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(username.substring(0, username.length() - 10));
        mFirebaseInstance.getReference("app_title").setValue("InstaProfileSaver");
*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AppVersion");

        reference.orderByChild("version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                     version=datas.getValue().toString();

                    System.out.println("Version main"+version);

                    Constants.displayLongToast(getActivity(),version);
                }

                if(BuildConfig.VERSION_CODE<Integer.parseInt(version))
                {
                    Constants.openPlayStore(getActivity(),"com.funcoders.Instadpsaver");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println("Version databaseError"+databaseError.toString());

            }
        });

        return version;
    }
}
