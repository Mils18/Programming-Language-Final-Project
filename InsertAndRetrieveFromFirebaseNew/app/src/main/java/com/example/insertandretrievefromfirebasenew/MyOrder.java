package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MyOrder extends AppCompatActivity {
    DatabaseReference ref,ref_c;
    String name_s;
    String shopName_s;
    ArrayList<Cart> list;
    MyAdapterMyOrder adapter;
    RecyclerView recyclerView;
    int totalPrice_i;

    private void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
        }
        try{
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
//                    assert user != null;
                    if(user!=null && user.getName().equals(name_s)){
                        shopName_s = user.getShopName();
                        System.out.println("NAME NEW ORDER1"+name_s);
                        System.out.println("SHOPNAME NEW ORDER1 "+shopName_s);
                    }
                    else{
                        System.out.println("NAME NEW ORDER2"+name_s);
                        System.out.println("SHOPNAME NEW ORDER2 "+shopName_s);
                        Toast.makeText(MyOrder.this,"error1 ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(MyOrder.this,"error2", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_buyer);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref_c = FirebaseDatabase.getInstance().getReference().child("Cart");
        getIntentValue();

//        ref_p = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerMyOrderBuyer);
        try{
            recyclerView.setLayoutManager( new LinearLayoutManager(this));
            ref_c.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<Cart>();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        Cart c = dataSnapshot1.getValue(Cart.class);
                        if(c!=null && c.getCustomerName().equals(name_s) && c.getStatus().equals("PAID") ){
                            list.add(c);
                        }
                    }
                    adapter = new MyAdapterMyOrder(MyOrder.this,list,name_s);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MyOrder.this, "Opsss.... Something is wrong1", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(MyOrder.this, "Opsss.... Something is wrong2", Toast.LENGTH_SHORT).show();
        }
        Button homeBtnNewOrderSeller = (Button)findViewById(R.id.homeBtnMyOrderAsBuyer);
        homeBtnNewOrderSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnProdDetail = (Button)findViewById(R.id.cartBtnMyOrderBuyer);
        cartBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button accountBtnNewOrderSeller = (Button)findViewById(R.id.accountBtnMyOrderAsBuyer);
        accountBtnNewOrderSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);

                startActivity(startIntent);
            }
        });
    }
}