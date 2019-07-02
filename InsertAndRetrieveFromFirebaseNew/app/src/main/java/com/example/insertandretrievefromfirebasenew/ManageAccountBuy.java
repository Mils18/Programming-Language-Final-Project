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

public class ManageAccountBuy extends AppCompatActivity {
//    DECLARES VARIABLES
    String name_s;
    String shopName_s;
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView balanceTextViewBuyScreen,accountNameTextViewBuyScreen;

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
                        String balance_s = Integer.toString(user.getBalance());
                        shopName_s = user.getShopName();
                        accountNameTextViewBuyScreen.setText(user.getName());
                        balanceTextViewBuyScreen.setText(balance_s);
                    }
                    else{
                        Toast.makeText(ManageAccountBuy.this,"Intent Error1", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ManageAccountBuy.this,"Intent Error2", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ex){
            Toast.makeText(ManageAccountBuy.this,"Intent Error3", Toast.LENGTH_LONG).show();
        }
    }

//  DETERMINE TYPE OF ACCOUNT AND DO INTENT TO RIGHT ACTIVITY
//  IF IT IS NOT UPGRADED YET, YOU ARE NOT ABLE TO DO SELLER AUTHORITIES, BUT NEED TO UPGRADE IT FIRST
    private void determineTypeOfAccount(){
        try{
            System.out.println(name_s);
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null){
                        if(user.getRole().equals("Regular")){
                            Intent startIntent = new Intent(getApplicationContext(),ManageAccountSellBefore.class);
                            startIntent.putExtra("NAME_INTENT",name_s);
                            startActivity(startIntent);
                        }
                        else if(user.getRole().equals("Business")){
                            Intent startIntent = new Intent(getApplicationContext(), ManageAccountSellAfter.class);
                            startIntent.putExtra("NAME_INTENT",name_s);
                            startActivity(startIntent);
                        }
                    }
                    else{
                        System.out.println("Error go to Sell Screen1");
                        Toast.makeText(ManageAccountBuy.this,"Error go to Sell Screen1", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            System.out.println("Error go to Sell Screen2");
            Toast.makeText(ManageAccountBuy.this,"Error go to Sell Screen2", Toast.LENGTH_LONG).show();
        }
    }

//    MAIN FUNCTION OF MANAGE ACCOUNT BUY CLASS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_account_buy);

        //declares all references, TextViews, EditTexts, and Buttons
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        accountNameTextViewBuyScreen = (TextView) findViewById(R.id.accountNameTextViewBuyScreen);
        balanceTextViewBuyScreen = (TextView) findViewById(R.id.balanceTextViewBuyScreen);
        getIntentValue();

//        BUTTONS CLICKED
        Button homeBtn = (Button)findViewById(R.id.homeBtnBuy);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnBuy = (Button)findViewById(R.id.cartBtnBuy);
        cartBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button topUpBtnBuyScreen = (Button)findViewById(R.id.topUpBtnBuyScreen);
        topUpBtnBuyScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),TopUp.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button myOrderBtnBuyScreen = (Button)findViewById(R.id.myOrderBtnBuyScreen);
        myOrderBtnBuyScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),MyOrder.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });


        Button goToSellBtn = (Button)findViewById(R.id.goToSellBtn);
        goToSellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                determineTypeOfAccount();
            }
        });

        Button signOutBtnBuyScreen = (Button)findViewById(R.id.signOutBtnBuyScreen);
        signOutBtnBuyScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),SignIn.class);
                startActivity(startIntent);
            }
        });

    }
}