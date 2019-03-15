package com.creaginetech.myshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creaginetech.myshop.models.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    
    private EditText edtNameCategory;
    private Button btnSaveCategory;
    private Toolbar addCategoryToolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
        widgets();

        //Toolbar back button
        addCategoryToolbar.setNavigationIcon(R.drawable.back_icon);
        addCategoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddCategoryActivity.this,CategoryActivity.class));
                finish();
            }
        });
        
        btnSaveCategory.setOnClickListener(this);
        
    }

    private void widgets() {
        edtNameCategory = findViewById(R.id.edtNameCategory);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        addCategoryToolbar = findViewById(R.id.addCategoryToolbar);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnSaveCategory){
            saveNewCategory();
        }
    }

    //[START SAVE NEW CATEGORY]
    private void saveNewCategory() {
        String categoryName = edtNameCategory.getText().toString();

        String catId = mDatabaseReference.push().getKey();

        Category category = new Category(categoryName,mFirebaseUser.getUid());

        //save category data to child "categorys"
        mDatabaseReference.child("categorys").child(catId).setValue(category).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddCategoryActivity.this, "Success to add new Category !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddCategoryActivity.this,CategoryActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCategoryActivity.this, "Failed to add Category !", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //[END SAVE NEW CATEGORY]
}
