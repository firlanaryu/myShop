package com.creaginetech.myshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creaginetech.myshop.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtShopName,edtShopAddress,edtShopEmail,edtShopPhone;
    private Button btnUpdateProfile;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        widgets();

        btnUpdateProfile.setOnClickListener(this);

    }

    private void widgets() {
        edtShopName = findViewById(R.id.edtUserNameShop);
        edtShopAddress = findViewById(R.id.edtShopAddress);
        edtShopEmail = findViewById(R.id.edtShopEmail);
        edtShopPhone = findViewById(R.id.edtShopPhone);
        btnUpdateProfile = findViewById(R.id.buttonUpdateProfile);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.buttonUpdateProfile){
            updateUser();
        }
    }

    private void updateUser() {
        startUpdateUser();

        Toast.makeText(this, "User Updated !", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AccountActivity.this,MainActivity.class));
        finish();

    }

    private void startUpdateUser() {
        String shopName = edtShopName.getText().toString();
        String shopAddress = edtShopAddress.getText().toString();
        String shopEmail = edtShopEmail.getText().toString();
        String shopPhone = edtShopPhone.getText().toString();

        wtiteUpdateUser(shopName,shopAddress,shopEmail,shopPhone);

    }

    private void wtiteUpdateUser(String shopName,String shopAddress,String shopEmail,String shopPhone) {
        String userId = mFirebaseUser.getUid();

        User user = new User(shopName,shopEmail,shopAddress,shopPhone);

        mDatabaseReference.child(userId).setValue(user);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                edtShopName.setText(user.getUsername());
                edtShopAddress.setText(user.getAddress());
                edtShopEmail.setText(user.getEmail());
                edtShopPhone.setText(user.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
