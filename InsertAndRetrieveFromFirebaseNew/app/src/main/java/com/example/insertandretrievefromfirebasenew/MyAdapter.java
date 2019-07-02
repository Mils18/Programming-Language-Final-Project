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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Product> products;
    private String customer_name;

    public MyAdapter(Context c, ArrayList<Product> p,String customer_name){
        this.context = c;
        this.products = p;
        this.customer_name = customer_name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.home_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.prodName.setText(products.get(position).getProdName());
        holder.price.setText("Rp. "+products.get(position).getPrice());
        holder.imageURI = holder.imageURI.parse(products.get(position).getImageURI());
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
        holder.onClick(position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView prodName,price;
        ImageView productPic;
        Button detailBtn;
        Uri imageURI;
        public MyViewHolder(View itemView){
            super(itemView);
            prodName = (TextView) itemView.findViewById(R.id.productNameCardTextView);
            price = (TextView) itemView.findViewById(R.id.priceCardTextView);
            productPic = (ImageView) itemView.findViewById(R.id.productImageCardView);
            detailBtn = (Button) itemView.findViewById(R.id.checkDetails);


        }
        public void onClick(final int position)
        {
            detailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                    System.out.println("position"+position);
                    Intent startIntent = new Intent(v.getContext(), ProductDetail.class);
                    String prodID = products.get(position).getproductID();
                    String prodName = products.get(position).getProdName();

                    System.out.println("prodName"+prodName);
                    startIntent.putExtra("PRODID_INTENT",prodID);
                    startIntent.putExtra("NAME_INTENT",customer_name);
                    context.startActivity(startIntent);
                }
            });

        }
    }
}

