package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ManageAccountSellAfter extends AppCompatActivity {
    String name_s;
    String shopName_s;
    DatabaseReference ref;
    TextView shopNameTextView;

    //    GET LOGGED IN NAME AND ALL THE VALUES NEEDED FROM PREVIOUS ACTIVITY IN ORDER TO KEEP TRACK OF WHICH ACCOUNT IS RUNNING
    private void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
        }
        try{
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null){
                        shopName_s = user.getShopName();
                        shopNameTextView.setText(shopName_s);
                    }
                    else{
                        Toast.makeText(ManageAccountSellAfter.this,"error1 ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(ManageAccountSellAfter.this,"error2", Toast.LENGTH_LONG).show();
        }
    }

//    MAIN ACTIVITY OF MANAGE ACCOUNT SELLER AFTER CLASS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_account_sell_after);
//        declares all references, TextViews, EditTexts, and Buttons
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        shopNameTextView = (TextView)findViewById(R.id.shopNameTextViewSellAfterScreen);
        getIntentValue();

//       BUTTONS CLICKED
        Button homeBtn = (Button)findViewById(R.id.homeBtnSellAfter);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnSellAfter = (Button)findViewById(R.id.cartBtnSellAfter);
        cartBtnSellAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

         Button goToBuyBtnAfter = (Button)findViewById(R.id.goToBuyBtnAfter);
        goToBuyBtnAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button  addProductBtn = (Button)findViewById(R.id.addProductBtn);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),AddProduct.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button accountBtnSellAfter = (Button)findViewById(R.id.accountBtnSellAfter);
        accountBtnSellAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button newOrderBtnSellAfter = (Button)findViewById(R.id.newOrderBtnSellAfter);
        newOrderBtnSellAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),NewOrder.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });
    }
}
