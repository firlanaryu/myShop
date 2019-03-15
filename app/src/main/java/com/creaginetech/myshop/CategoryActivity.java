package com.creaginetech.myshop;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar categoryToolbar;
    private FloatingActionButton btnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        widgets();

        //Toolbar back button
        categoryToolbar.setNavigationIcon(R.drawable.back_icon);
        categoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this,MainActivity.class));
                finish();
            }
        });

        btnAddCategory.setOnClickListener(this);

    }

    private void widgets() {
        btnAddCategory = findViewById(R.id.btnAddCategory);
        categoryToolbar = findViewById(R.id.categoryToolbar);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnAddCategory){
            startActivity(new Intent(CategoryActivity.this,AddCategoryActivity.class));
            finish();
        }
    }
}
