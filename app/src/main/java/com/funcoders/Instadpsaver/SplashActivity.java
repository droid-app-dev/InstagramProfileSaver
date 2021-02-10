package com.funcoders.Instadpsaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        version=findViewById(R.id.version_tv);

        version.setText("Version "+BuildConfig.VERSION_NAME);


      //  getSupportActionBar().hide();
      //  getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));

       // CheckForUpdate();

        int SPLASH_SCREEN_TIMEOUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}