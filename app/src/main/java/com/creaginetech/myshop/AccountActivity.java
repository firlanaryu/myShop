package com.creaginetech.myshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.creaginetech.myshop.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtShopName,edtShopAddress,edtShopEmail,edtShopPhone;
    private Button btnUpdateProfile;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private CircleImageView imageProfile;
    private Uri mainImageUri = null;
    private boolean isChanged = false;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("shops");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        widgets();

        //Toolbar back button
        toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this,MainActivity.class));
                finish();
            }
        });

        btnUpdateProfile.setOnClickListener(this);
        imageProfile.setOnClickListener(this);

    }

    private void widgets() {
        edtShopName = findViewById(R.id.edtUserNameShop);
        edtShopAddress = findViewById(R.id.edtShopAddress);
        edtShopEmail = findViewById(R.id.edtShopEmail);
        edtShopPhone = findViewById(R.id.edtShopPhone);
        btnUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        toolbar = findViewById(R.id.accountToolbar);
        progressBar = findViewById(R.id.progressBarAccount);
        imageProfile= findViewById(R.id.imageProfile);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.buttonUpdateProfile){
            updateUser();
        }
        else if (i == R.id.imageProfile){
            setImageProfile();
        }
    }

    //[START TO RETRIEVE IMAGE FROM STORAGE] | image profile - 1
    private void setImageProfile() {
        //for check permission if sdk low from marsmalllow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(AccountActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(AccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            } else {
                // start picker to get image for cropping and then use the image in cropping activity
                bringImagePicture();
            }
        } else {
            bringImagePicture();
        }
    }
    //Crop image | image profile - 2
    private void bringImagePicture() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(AccountActivity.this);
    }

    //[START update user]
    private void updateUser() {

        final String shopName = edtShopName.getText().toString();

        startUpdateUser();

        Toast.makeText(this, "User Updated !", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AccountActivity.this,MainActivity.class));
        finish();

    }

    private void startUpdateUser() {

        //UPLOAD IMAGE TO FIREBASE STORAGE
        uploadImage();

        //[START to download image URL from Firebase Storage]
        final StorageReference ref = storageReference.child("imagesProfile/").child(mFirebaseUser.getUid() + ".jpg");
        UploadTask uploadTask = ref.putFile(mainImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult(); //Download URL

                    String shopName = edtShopName.getText().toString();
                    String shopAddress = edtShopAddress.getText().toString();
                    String shopEmail = edtShopEmail.getText().toString();
                    String shopPhone = edtShopPhone.getText().toString();

                    //downloadUri.toString() --> convert image to string
                    wtiteUpdateUser(shopName,shopAddress,shopEmail,shopPhone,downloadUri.toString());

                } else {
                    // Handle failures
                    // ...
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(AccountActivity.this, "Error : " +errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //[END to download image URL from Firebase Storage]

    }

    private void uploadImage() {

        StorageReference imagePath = storageReference.child("imagesProfile/").child(mFirebaseUser.getUid() + ".jpg");
        UploadTask uploadTask = imagePath.putFile(mainImageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(AccountActivity.this, "failed upload image profile", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(AccountActivity.this, "Success to uplad image profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void wtiteUpdateUser(String shopName,String shopAddress,String shopEmail,String shopPhone,String imageProfileUrl) {
        String userId = mFirebaseUser.getUid();
        User user = new User(shopName,shopEmail,shopAddress,shopPhone,imageProfileUrl);
        mDatabaseReference.child(userId).setValue(user);
    }
    //[END update user]

    //ACCOUNT ACTIVITY START TO LISTEN AND RETRIEVE DATA USER
    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.VISIBLE);
        mDatabaseReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                edtShopName.setText(user.getUsername());
                edtShopAddress.setText(user.getAddress());
                edtShopEmail.setText(user.getEmail());
                edtShopPhone.setText(user.getPhone());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //set image crop to image profile circle | image profile - 3
    // to image profile - 4 Add to manifest permission
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                //set circleImage for change the picture profile
                imageProfile.setImageURI(mainImageUri);
                // if image selected
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    //[END TO RETRIEVE IMAGE FROM STORAGE]
}
