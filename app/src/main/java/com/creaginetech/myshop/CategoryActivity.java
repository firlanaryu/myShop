package com.creaginetech.myshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creaginetech.myshop.models.Category;
import com.creaginetech.myshop.viewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private Toolbar categoryToolbar;
    private FloatingActionButton btnAddCategory;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        widgets();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.categoryItem);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

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

    private void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference().child("categorys").orderByChild("shopId").equalTo(firebaseUser.getUid());

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(query, new SnapshotParser<Category>() {
            @NonNull
            @Override
            public Category parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new Category(snapshot.child("nameCategory").getValue().toString());
            }
        }).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {

//                final DatabaseReference catRef = getRef(position);

                holder.txtCategory(model.getNameCategory());
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_category,viewGroup,false);

                return new CategoryViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

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

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

}
