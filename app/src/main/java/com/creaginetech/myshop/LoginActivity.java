package com.creaginetech.myshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.creaginetech.myshop.common.Common;
import com.creaginetech.myshop.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText edtEmail,edtPassword;
    private Button btnLogin,btnRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        edtEmail = findViewById(R.id.loginEmail);
        edtPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        progressBar = findViewById(R.id.progressBarLogin);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnLogin){
            signIn();
        }
        else if (i == R.id.btnRegister){
            registerActivity();
        }
    }

    private void registerActivity() {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    private void signIn() {
        Log.d(TAG,"SignIn ");
        if (!validateForm()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUser:onComplete: " + task.isSuccessful());
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()){
                            onAuthsuccess(task.getResult().getUser());
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Log.d(TAG,"Error :  " + errorMessage);
                            Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void onAuthsuccess(FirebaseUser currentUser) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Common.userId = userId;

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {

        boolean result = true;
        if (TextUtils.isEmpty(edtEmail.getText().toString())){
            edtEmail.setError("Required");
            result=false;
        } else {
            edtEmail.setError(null);
        }

        if (TextUtils.isEmpty(edtPassword.getText().toString())){
            edtPassword.setError("Required");
            result=false;
        } else {
            edtPassword.setError(null);
        }

        return result;
    }

}
