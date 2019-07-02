package com.example.insertandretrievefromfirebasenew;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterMyOrder extends RecyclerView.Adapter<MyAdapterMyOrder.MyViewHolder> {
    private Context context;
    private ArrayList<Cart> carts;
    private String name_s,prodID,buyer_s,seller_s;
    boolean isFinishSubtract = false;
    boolean isFinishAddBalance = false;
    boolean isFinishAddBalance2 = false;
    boolean sufficient_stock;
    DatabaseReference ref_p ;
    DatabaseReference ref;

    public MyAdapterMyOrder(Context c, ArrayList<Cart> c_list, String name_s){
        this.context = c;
        this.carts = c_list;
        this.name_s = name_s;
    }


    @NonNull
    @Override
    public MyAdapterMyOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_order_buyer_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.invoiceNum.setText("Invoice Number : "+carts.get(position).getInvoiceNumber());
        holder.shopName.setText("Shop : "+carts.get(position).getShopName());
        holder.productName.setText(carts.get(position).getProdName());
        holder.quantityProdPrice.setText("Quantity x Product Price (" + carts.get(position).getQuantity() + "x" + carts.get(position).getProdPrice()+")");
        holder.totalPrice.setText("Total Transaction : "+carts.get(position).getProdPrice()*carts.get(position).getQuantity());
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
        TextView invoiceNum,shopName,totalPrice,productName,quantityProdPrice;
        ImageView productPic;
        Uri imageURI;


        public MyViewHolder(View itemView) {
            super(itemView);
            invoiceNum = (TextView) itemView.findViewById(R.id.invoiceNumMyOrderBuyerCardview);
            productName = (TextView) itemView.findViewById(R.id.prodNameMyOrderBuyerCardview);
            shopName = (TextView) itemView.findViewById(R.id.shopNameMyOrderBuyerCardview);
            quantityProdPrice = (TextView) itemView.findViewById(R.id.quantityProdMyOrderBuyerCardview);
            totalPrice = (TextView) itemView.findViewById(R.id.totalPriceMyOrderBuyerCardview);
            productPic = (ImageView) itemView.findViewById(R.id.imageViewMyOrderBuyerCardView);

        }
    }
}
