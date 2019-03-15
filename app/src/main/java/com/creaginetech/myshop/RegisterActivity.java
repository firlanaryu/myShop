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

import com.creaginetech.myshop.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText edtEmail,edtPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        edtEmail = findViewById(R.id.registerEmail);
        edtPassword = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegister);

        progressBar = findViewById(R.id.progressBarLogin);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnRegister){
            registerUser();
        }
    }

    // [Start register user]
    private void registerUser() {
        Log.d(TAG,"SignUp");
        if (!validateForm()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUser:onComplete: " + task.isSuccessful());
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()){
                            onAuthsuccess(task.getResult().getUser());
                            Toast.makeText(RegisterActivity.this, "Success to sign up", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void onAuthsuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        //write new user
        writeNewUser(user.getUid(),username,user.getEmail());
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        finish();
    }

    private void writeNewUser(String uid,String username,String email) {
        User user = new User(username,email);
        mDatabaseReference.child("shops").child(uid).setValue(user);
    }

    //create username from email
    private String usernameFromEmail(String email) {

        if (email.contains("@")){
            return email.split("@")[0];
        } else {
            return email;
        }

    }
    //[END register user]

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
