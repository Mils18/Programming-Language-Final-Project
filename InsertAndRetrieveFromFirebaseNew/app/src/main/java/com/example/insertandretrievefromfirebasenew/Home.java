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

public class Home extends AppCompatActivity {
    //    declares variables
    DatabaseReference ref,ref_p;
    String name_s;
    String shopName_s;
    ArrayList<Product> list;
    MyAdapter adapter;
    RecyclerView recyclerView;

    //    GET LOGGED IN NAME AND ALL THE VALUES NEEDED FROM PREVIOUS ACTIVITY IN ORDER TO KEEP TRACK OF WHICH ACCOUNT IS RUNNING
    private void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
        }
    }

//    MAIN FUNCTION OF HOME CLASS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

//        declares all references, TextViews, EditTexts, and Buttons
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref_p = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        getIntentValue();

//      RECYCLER VIEW TO SHOW ALL PRODUCTS UPLOADED
        try{
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
            ref_p.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<Product>();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        Product p = dataSnapshot1.getValue(Product.class);
//                        assert p != null;
                        System.out.println("Prodname"+p.getProdName());
                        System.out.println("Desc"+p.getDescription());

                        list.add(p);
                    }
                    adapter = new MyAdapter(Home.this,list,name_s);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Opsss.... Something is wrong1", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(Home.this, "Opsss.... Something is wrong2", Toast.LENGTH_SHORT).show();
        }


//        BUTTONS
        Button accountBtn = (Button)findViewById(R.id.accountBtn);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnProdDetail = (Button)findViewById(R.id.cartBtnHome);
        cartBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });
    }
}
