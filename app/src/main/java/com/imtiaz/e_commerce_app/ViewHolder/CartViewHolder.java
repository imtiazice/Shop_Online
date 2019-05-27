package com.imtiaz.e_commerce_app.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.imtiaz.e_commerce_app.Interface.ItemClickListner;
import com.imtiaz.e_commerce_app.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);

    }

    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
