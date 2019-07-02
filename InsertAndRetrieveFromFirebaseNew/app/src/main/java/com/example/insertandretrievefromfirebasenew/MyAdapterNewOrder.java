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

public class MyAdapterNewOrder extends RecyclerView.Adapter<MyAdapterNewOrder.MyViewHolder> {
    private Context context;
    private ArrayList<Cart> carts;
    private String name_s,prodID,buyer_s,seller_s;
    boolean isFinishSubtract = false;
    boolean isFinishAddBalance = false;
    boolean isFinishAddBalance2 = false;

    boolean isFinishUpdateCart = false;
    boolean sufficient_stock;
    DatabaseReference ref_p ;
    DatabaseReference ref_c ;
    Cart cart;

    DatabaseReference ref;

    private void updateCartStatusToSuccess(final int position){
        ref_c = FirebaseDatabase.getInstance().getReference().child("Cart");
        try{
            ref_c.child(carts.get(position).getBookingCode()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    if(cart != null && cart.getBookingCode().equals(carts.get(position).getBookingCode()) && !isFinishUpdateCart){
                        System.out.println("cart.getBookingCode() "+cart.getBookingCode());
                        cart.setStatus("SUCCESS");
                        ref_c.child(cart.getBookingCode()).setValue(cart);
                        isFinishUpdateCart = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void updateCartStatusToDeclined(final int position){
        ref_c = FirebaseDatabase.getInstance().getReference().child("Cart");
        try{
            ref_c.child(carts.get(position).getBookingCode()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    if(cart != null && cart.getBookingCode().equals(carts.get(position).getBookingCode()) && !isFinishUpdateCart){
                        System.out.println("cart.getBookingCode() "+cart.getBookingCode());
                        cart.setStatus("DECLINED");
                        ref_c.child(cart.getBookingCode()).setValue(cart);
                        isFinishUpdateCart = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addSellerBalance(final int position){
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        seller_s = name_s;
        System.out.println("SELLER NAME : "+seller_s);

        try{
            ref.child(seller_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null && user.getName().equals(seller_s) && !isFinishAddBalance){
//                        String topUpAmount_s = topUpAmountEditText.getText().toString();
                        System.out.println("add balance");
                        int new_balance = user.getBalance() + carts.get(position).getProdPrice()*carts.get(position).getQuantity();
                        System.out.println("new_balance" + new_balance);
                        user.setBalance(new_balance);
                        ref.child(seller_s).setValue(user);
                        updateCartStatusToSuccess(position);
                        isFinishAddBalance = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
    private boolean checkStock(Product p,int position){
        if (carts.get(position).getQuantity() <= p.getStock()){
            return true;
        }
        else{
            return false;
        }
    }
    private void subtractStock(final int position){
        ref_p = FirebaseDatabase.getInstance().getReference().child("Products");
        prodID = carts.get(position).getProdID();
        System.out.println("prodID"+prodID);
//        try{
            ref_p.child(prodID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if(product != null && product.getproductID().equals(prodID) && !isFinishSubtract && checkStock(product,position)){
//                        String topUpAmount_s = topUpAmountEditText.getText().toString();
                        System.out.println("subtract");
                        int new_stock = product.getStock() - carts.get(position).getQuantity();
                        System.out.println("newstock" + new_stock);
                        product.setStock(new_stock);
                        ref_p.child(prodID).setValue(product);
                        addSellerBalance(position);
                        isFinishSubtract = true;
                        sufficient_stock = true;
                    }
                    else{
                        sufficient_stock = false;
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }



    private void addBuyerBalance(final int position){
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        buyer_s = carts.get(position).getCustomerName();
//        try{
            ref.child(buyer_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null && user.getName().equals(buyer_s) && !isFinishAddBalance){
//                        String topUpAmount_s = topUpAmountEditText.getText().toString();
                        System.out.println("add balance");
                        int new_balance = user.getBalance() + carts.get(position).getProdPrice()*carts.get(position).getQuantity();
                        System.out.println("new_balance" + new_balance);
                        user.setBalance(new_balance);

                        ref.child(buyer_s).setValue(user);
                        isFinishAddBalance = true;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }
    private void backToSeller(final int position){
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.child(name_s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null && user.getName().equals(name_s) && !isFinishAddBalance2){
//                        String topUpAmount_s = topUpAmountEditText.getText().toString();
                    System.out.println("add balance");
                    int new_balance = user.getBalance() + 0;
                    System.out.println("new_balance" + new_balance);
                    user.setBalance(new_balance);

                    ref.child(name_s).setValue(user);
                    updateCartStatusToDeclined(position);

                    isFinishAddBalance2 = true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    public MyAdapterNewOrder(Context c, ArrayList<Cart> c_list, String name_s){
        this.context = c;
        this.carts = c_list;
        this.name_s = name_s;
    }


    @NonNull
    @Override
    public MyAdapterNewOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.new_order_seller_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.invoiceNum.setText("Invoice Number : "+carts.get(position).getInvoiceNumber());
        holder.buyerName.setText("Buyer : "+carts.get(position).getCustomerName());
        holder.productName.setText(carts.get(position).getProdName());
        holder.quantityProdPrice.setText("Quantity x Product Price (" + carts.get(position).getQuantity() + "x" + carts.get(position).getProdPrice()+")");
        holder.totalPrice.setText("Total Transaction : "+carts.get(position).getProdPrice()*carts.get(position).getQuantity());
        holder.onClick(position);
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
        TextView invoiceNum,buyerName,totalPrice,productName,quantityProdPrice;
        Button proceedBtn,declineBtn,testBtn;
        ImageView productPic;
        Uri imageURI;


        public MyViewHolder(View itemView){
            super(itemView);
                invoiceNum = (TextView) itemView.findViewById(R.id.invoiceNumNewOrderSellerCardview);
                productName = (TextView) itemView.findViewById(R.id.prodNameTextViewNewOrderSellerCardview);
                buyerName = (TextView) itemView.findViewById(R.id.BuyerNameTextViewNewOrderSellerCardview);
                quantityProdPrice = (TextView) itemView.findViewById(R.id.quantityProdPriceNewOrderSellerCardview);
                totalPrice = (TextView) itemView.findViewById(R.id.totalPriceTextViewNewOrderSellerCardview);
                proceedBtn = (Button) itemView.findViewById(R.id.proceedBtnNewOrder);
                declineBtn = (Button) itemView.findViewById(R.id.declineBtnNewOrder);
                productPic = (ImageView) itemView.findViewById(R.id.imageViewNewOrderSellerCardView);

        }
        public void onClick(final int position)
        {
            proceedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subtractStock(position);
                    if(sufficient_stock) {
                        Intent startIntent = new Intent(v.getContext(), TransactionCompleteNewOrder.class);
                        startIntent.putExtra("NAME_INTENT", name_s);
                        context.startActivity(startIntent);
                    }
                    else {
                        Toast.makeText(context,"insufficient Stock", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, position + " is clicked", Toast.LENGTH_SHORT).show();
                    String invoiceNumber_intent = carts.get(position).getInvoiceNumber();
                    addBuyerBalance(position);
                    backToSeller(position);
                    Intent startIntent = new Intent(v.getContext(), TransactionCompleteNewOrder.class);
                    startIntent.putExtra("NAME_INTENT", name_s);
                    context.startActivity(startIntent);
                }
            });

        }
    }
}
