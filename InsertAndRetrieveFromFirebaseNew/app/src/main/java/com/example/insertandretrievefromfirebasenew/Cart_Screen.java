package com.example.insertandretrievefromfirebasenew;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

//Add Product Class
public class Cart_Screen extends AppCompatActivity {
    DatabaseReference ref,ref_p,ref_c,ref_o;
    String name_s,invoiceNumber_s,shopName_s;
    int customerBalance_i;
    ArrayList<Cart> cartList;
    MyAdapterCart adapter_cart;
    RecyclerView recyclerViewCart;
    TextView totalPriceTextView;
    int totalPrice_i, newBalance;
    User user;
    boolean letsDoTransaction,subtractStockBool;

    //function to generate random product Invoice
    private void generateRandomInvoiceNum(){
        final int randomNum = new Random().nextInt(90000) + 10000;
        String randomNum_s = Integer.toString(randomNum);
        invoiceNumber_s = "INV"+randomNum_s;
    }

    //changes all specific person's products' status from 'UNPAID" to "PAID" and upload to Firebase
    private void doTransaction(){
        try{
            ref_c.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int num = 0;
                    if (letsDoTransaction) {
                        letsDoTransaction = false;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            System.out.println(num += 1);
                            Cart c = dataSnapshot1.getValue(Cart.class);
                            if (c != null && c.getCustomerName().equals(name_s) && c.getStatus().equals("UNPAID")) {
                                generateRandomInvoiceNum();
                                c.setInvoiceNumber(invoiceNumber_s);
                                c.setStatus("PAID");
                                ref_c.child(c.getBookingCode()).setValue(c);
                            }
                        }

                        Toast.makeText(Cart_Screen.this, "Transaction Successful ...", Toast.LENGTH_LONG).show();
                        Intent startIntent = new Intent(getApplicationContext(), TransactionSuccessful.class);
                        startIntent.putExtra("NAME_INTENT", name_s);
                        startActivity(startIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Cart_Screen.this, "Opsss.... Something is wrong1", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(Cart_Screen.this, "Opsss.... Something is wrong2", Toast.LENGTH_SHORT).show();
        }
    }

//    GET LOGGED IN NAME AND ALL THE VALUES NEEDED FROM PREVIOUS ACTIVITY IN ORDER TO KEEP TRACK OF WHICH ACCOUNT IS RUNNING
    private void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
        }

        try {
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user!=null){
                    customerBalance_i = user.getBalance();
                    System.out.println("customerBalance_i_cartscreen"+customerBalance_i);
                 }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    } catch (Exception ex) {
        Toast.makeText(Cart_Screen.this, "getBalance", Toast.LENGTH_LONG).show();
        }

    }

//    Main function of Cart Screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        //declares all references, TextViews, EditTexts, and Buttons
        letsDoTransaction = false;
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref_c = FirebaseDatabase.getInstance().getReference().child("Cart");
        ref_o = FirebaseDatabase.getInstance().getReference().child("Orders");
        totalPriceTextView = (TextView) findViewById(R.id.totalPriceTextViewCart);
        getIntentValue();


        //BUTTONS
        Button homeBtn = (Button)findViewById(R.id.homeBtnCartScreen);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });
        Button accountBtn = (Button)findViewById(R.id.accountBtnCartScreen);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });
        Button checkOutBtnCart = (Button)findViewById(R.id.checkOutBtnCart);
        checkOutBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customerBalance_i >= totalPrice_i){
                    newBalance = customerBalance_i - totalPrice_i;
                    user.setBalance(newBalance);
                    ref.child(name_s).setValue(user);
                    letsDoTransaction = true;
                    subtractStockBool = true;
                    doTransaction();
                }
                else{
                    Toast.makeText(Cart_Screen.this, "Insufficient Balance, Please top up first", Toast.LENGTH_SHORT).show();
                }
            }
        });

//      RECYCLER VIEW TO SHOW ALL PRODUCTS IN THE SPECIFIC PERSON'S CART
        recyclerViewCart = (RecyclerView) findViewById(R.id.myRecyclerCart);
        try{
            recyclerViewCart.setLayoutManager( new LinearLayoutManager(this));
            ref_c.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cartList = new ArrayList<Cart>();

                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        Cart c = dataSnapshot1.getValue(Cart.class);
                        //      PUT ALL THE PRODUCTS WITH SPECIFIC NAME AND UNPAID STATUS INTO CARTLIST\
                        if(c!=null && c.getCustomerName().equals(name_s) && c.getStatus().equals("UNPAID") ){
                            cartList.add(c);
                            int subTotal =  c.getQuantity()*c.getProdPrice();
                            totalPrice_i = totalPrice_i + subTotal;
                        }
                    }
                    totalPriceTextView.setText("Rp. "+totalPrice_i);
                    adapter_cart = new MyAdapterCart(Cart_Screen.this,cartList,name_s);
                    recyclerViewCart.setAdapter(adapter_cart);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Cart_Screen.this, "Recycle error", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(Cart_Screen.this, "Opsss.... Something is wrong2", Toast.LENGTH_SHORT).show();
        }
    }
}