package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ManageAccountSellBefore extends AppCompatActivity {
    String name_s,shopName_s;
    DatabaseReference ref;

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
                        System.out.println("NAME "+name_s);
                        System.out.println("SHOPNAME "+shopName_s);
                    }
                    else{
                        Toast.makeText(ManageAccountSellBefore.this,"error1 ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(ManageAccountSellBefore.this,"error2", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_account_sell_before);

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        getIntentValue();

        Button homeBtn = (Button)findViewById(R.id.homeBtnSellBefore);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnProdDetail = (Button)findViewById(R.id.cartBtnSellBefore);
        cartBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });


        Button accountBtnSellBefore = (Button)findViewById(R.id.accountBtnSellBefore);
        accountBtnSellBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);

                startActivity(startIntent);
            }
        });

        Button goToBuyBtnBefore = (Button)findViewById(R.id.goToBuyBtnBefore);
        goToBuyBtnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button upgradeBtn = (Button)findViewById(R.id.upgradeAccountBtn);
        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Upgrade.class);
                startIntent.putExtra("NAME_INTENT",name_s);

                startActivity(startIntent);
            }
        });
    }
}
