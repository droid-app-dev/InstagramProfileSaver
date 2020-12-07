package com.funcoders.Instadpsaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.funcoders.Instadpsaver.RoomDb.TaskAppDatabase;
import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.bean.Contatctdatapost;
import com.funcoders.Instadpsaver.common.Constants;
import com.funcoders.Instadpsaver.contactsevice.ContactService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maksim88.passwordedittext.PasswordEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button btn_Login, btn_Register,btn_passwordreset;
    TextInputEditText et_Email;
    TextView tv_version,tv_passwordreset,tv_Login;
    PasswordEditText et_Password, et_ConfirmPasword;
    private FirebaseAuth mAuth;
    private String TAG = "LoginActivity";
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog = null;
    String appVersionName = "";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TaskAppDatabase appDatabase;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = LoginActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryDark));

        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        mAuth = FirebaseAuth.getInstance();
        appVersionName = BuildConfig.VERSION_NAME;
        appDatabase = TaskAppDatabase.getInstance(LoginActivity.this);

        mFirebaseInstance = FirebaseDatabase.getInstance();




        btn_Login = findViewById(R.id.btn_login);
        tv_version = findViewById(R.id.tv_version);
        tv_Login = findViewById(R.id.tv_Login);
        btn_Register = findViewById(R.id.btn_register);
        btn_passwordreset = findViewById(R.id.btn_passwordreset);
        et_Email = findViewById(R.id.user_email);
        et_Password = findViewById(R.id.et_password);
        et_ConfirmPasword = findViewById(R.id.et_confirm_password);
        tv_passwordreset = findViewById(R.id.tv_passwordreset);

        tv_version.setText(appVersionName);


        if (sharedPreferences.getBoolean(Constants.isfromRegister, false)) {
            btn_Register.setVisibility(View.GONE);
            btn_Login.setVisibility(View.VISIBLE);
            et_ConfirmPasword.setVisibility(View.GONE);
            tv_passwordreset.setVisibility(View.VISIBLE);
            tv_Login.setVisibility(View.GONE);
            et_Email.setText(sharedPreferences.getString(Constants.userName, ""));
            et_Password.setText(sharedPreferences.getString(Constants.userPassword, ""));
        } else {
            btn_Register.setVisibility(View.VISIBLE);
            btn_Login.setVisibility(View.GONE);
            tv_passwordreset.setVisibility(View.GONE);
            et_ConfirmPasword.setVisibility(View.VISIBLE);
            tv_Login.setVisibility(View.VISIBLE);
        }

        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_ConfirmPasword.setVisibility(View.GONE);
                btn_Register.setVisibility(View.GONE);
                btn_Login.setVisibility(View.VISIBLE);
                tv_Login.setVisibility(View.GONE);
                tv_passwordreset.setVisibility(View.VISIBLE);

            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateLogin()) {

                    if (Constants.isNetworkAvailable(LoginActivity.this)) {

                        progressDialog = Constants.showProgressDialog(LoginActivity.this, "");



                        sigin(et_Email.getText().toString(), et_Password.getText().toString());
                    } else {

                        Toast.makeText(LoginActivity.this, "No Network..!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }


        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateRegister()) {
                    if (Constants.isNetworkAvailable(LoginActivity.this)) {


                        progressDialog = Constants.showProgressDialog(LoginActivity.this, "");
                        authenticateuser(et_Email.getText().toString(), et_Password.getText().toString());

                    } else {

                        Toast.makeText(LoginActivity.this, "No Network..!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });

        tv_passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_Password.setVisibility(View.GONE);
                btn_Login.setVisibility(View.GONE);
                tv_version.setVisibility(View.GONE);
                btn_passwordreset.setVisibility(View.VISIBLE);
            }
        });

        btn_passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isValid(et_Email.getText().toString()))
                {
                    if (Constants.isNetworkAvailable(LoginActivity.this)) {

                        progressDialog = Constants.showProgressDialog(LoginActivity.this, "");

                        resetPassword(et_Email.getText().toString());

                    }
                    else {

                        Toast.makeText(LoginActivity.this, "No Network..!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {

                    et_Email.setError("Email not Valid");
                }


            }


        });
    }
    private void resetPassword(String email) {

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            Log.d(TAG, "Email sent.");

                            et_Password.setVisibility(View.VISIBLE);
                            btn_Login.setVisibility(View.VISIBLE);
                            tv_version.setVisibility(View.VISIBLE);
                            btn_passwordreset.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Link will be sended to your mail",
                                    Toast.LENGTH_SHORT).show();

                        }else {

                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Email Not Exist..!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            System.out.println("Current user" + currentUser.getEmail());

        // updateUI(currentUser);
    }


    public void authenticateuser(final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.isfromRegister, true);
                            editor.putString(Constants.userName, email);
                            editor.putString(Constants.userPassword, password);
                            editor.apply();


                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "User Already Exist",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });

    }

    public void sigin(final String email, final String password) {




      // for (int i = 0; i < contactModelList.size(); i++) {

          //  storetoFirebaseDB();

      //  }



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                           /* System.out.println(user.getPhotoUrl());
                            System.out.println(user.getEmail());
                            System.out.println(user.getMetadata());
                            System.out.println(user.getDisplayName());*/

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.isfromRegister, true);
                            editor.putString(Constants.userName, email);
                            editor.putString(Constants.userPassword, password);
                            editor.apply();

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(LoginActivity.this, "Invalid Credentials..!",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });



    }


    private boolean validateRegister() {
        boolean validate = false;

        if (TextUtils.isEmpty(et_Email.getText().toString())) {
            validate = false;
            et_Email.setError("Email Can't Empty");
        } else if (!isValid(et_Email.getText().toString())) {
            validate = false;
            et_Email.setError("Enter Valid Email");
        } else if (TextUtils.isEmpty(et_Password.getText().toString())) {
            validate = false;
            et_Password.setError("Password Can't Empty");
        } else if (TextUtils.isEmpty(et_ConfirmPasword.getText().toString())) {
            validate = false;
            et_ConfirmPasword.setError("ConfirmPassword Can't Empty");
        } else

            {
            if (!et_Password.getText().toString().equals(et_ConfirmPasword.getText().toString())) {
                validate = false;
                et_ConfirmPasword.setError("Password Missmatch");
            }  else if(et_Password.getText().toString().length()>=6)
            {
                validate = true;

            }else  {
                et_ConfirmPasword.setError("Password Should be 6 Characters");

            }
        }

        return validate;
    }

    private boolean validateLogin() {
        boolean validate = false;

        if (TextUtils.isEmpty(et_Email.getText().toString())) {
            validate = false;
            et_Email.setError("Email Can't Empty");
        } else if (TextUtils.isEmpty(et_Password.getText().toString())) {
            validate = false;
            et_Password.setError("Password Can't Empty");
        } else {

            if (!isValid(et_Email.getText().toString())) {
                et_Email.setError("Enter Valid Email");
                validate = false;
            } else {

                validate = true;

            }

        }


        return validate;
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }



    /*private void updateUser(String name, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }*/



}