package com.creaginetech.myshop.viewHolder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.creaginetech.myshop.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout root;
    public TextView txtCategory;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        root = itemView.findViewById(R.id.list_category);
        txtCategory = itemView.findViewById(R.id.txtCategory);
    }

    public void txtCategory(String nameCategory) {
        txtCategory.setText(nameCategory);
    }
}
