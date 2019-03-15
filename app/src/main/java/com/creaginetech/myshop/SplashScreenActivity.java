package com.creaginetech.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final android.os.Handler waitHandler = new android.os.Handler();
    private final Runnable waitCallback = new Runnable() {
        @Override
        public void run() {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null){
                sendToMain();
            } else {
                sendToLogin();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

    }

    private void sendToLogin() {
        startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
        finish();
    }

    private void sendToMain() {
        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        waitHandler.postDelayed(waitCallback,2000); //2000 = 2 SECOND loading
    }

    @Override
    protected void onDestroy() {
        waitHandler.removeCallbacks(waitCallback);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //dikosongi supaya tidak bisa back waktu splash screen
//        super.onBackPressed();
    }
}
