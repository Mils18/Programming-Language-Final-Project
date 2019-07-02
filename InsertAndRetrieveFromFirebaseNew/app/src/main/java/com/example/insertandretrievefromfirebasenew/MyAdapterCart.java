package com.example.insertandretrievefromfirebasenew;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.MyViewHolder> {
    private Context context;
    private ArrayList<Cart> carts;
    private String customer_name;

    public MyAdapterCart(Context c, ArrayList<Cart> p,String customer_name){
        this.context = c;
        this.carts = p;
        this.customer_name = customer_name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.prodName.setText(carts.get(position).getProdName());
        holder.prodPrice.setText("Rp. "+carts.get(position).getProdPrice());
        holder.quantity.setText("Quantity : "+carts.get(position).getQuantity());
        holder.imageURI = holder.imageURI.parse(carts.get(position).getImageURI());
        Picasso.with(context).load(holder.imageURI).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.productPic, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                System.out.println("SUCCESS LOAD IMAGE");
            }

            @Override
            public void onError() {
                System.out.println("ERROR LOAD IMAGE");

            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView prodName,prodPrice,quantity;
        Uri imageURI;
        ImageView productPic;

        public MyViewHolder(View itemView){
            super(itemView);
            prodName = (TextView) itemView.findViewById(R.id.prodNameTextViewCartCardView);
            prodPrice = (TextView) itemView.findViewById(R.id.prodPriceTextViewCartCardView);
            quantity = (TextView) itemView.findViewById(R.id.quantityTextViewCartCardView);
            productPic = (ImageView) itemView.findViewById(R.id.imageViewCartCardview);
        }
    }
}

