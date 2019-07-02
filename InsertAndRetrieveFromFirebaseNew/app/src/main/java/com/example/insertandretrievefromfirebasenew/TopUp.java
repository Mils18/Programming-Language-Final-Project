package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TopUp extends AppCompatActivity {
    DatabaseReference ref;
    String name_s;
    EditText topUpAmountEditText;
    boolean isFinishTopUp = false;

    private void topUp(){
        try{
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null && user.getName().equals(name_s) && !isFinishTopUp){

                        String topUpAmount_s = topUpAmountEditText.getText().toString();
                        int topUpAmount_i = Integer.parseInt(topUpAmount_s);
                        if (topUpAmount_i == 0){

                        }else{
                            int balance_new = user.getBalance() + topUpAmount_i;
                            user.setBalance(balance_new);

                            ref.child(name_s).setValue(user);
                            Toast.makeText(TopUp.this,"TopUp Successful", Toast.LENGTH_SHORT).show();
//                        Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
//                        startIntent.putExtra("NAME_INTENT",name_s);
//                        startActivity(startIntent);
                            isFinishTopUp = true;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(TopUp.this,"Error to Update Data", Toast.LENGTH_LONG).show();
        }
    }

    private void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_up_screen);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        getIntentValue();
        topUpAmountEditText = (EditText) findViewById(R.id.enterAmountEditTextTopUp);

        Button topUpBtnTopUp = (Button)findViewById(R.id.topUpBtnTopUp);
        topUpBtnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topUp();
            }
        });

        Button homeBtn = (Button)findViewById(R.id.homeBtnTopUp);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnTopUp = (Button)findViewById(R.id.cartBtnTopUp);
        cartBtnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),Cart.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button accountBtnTopUpScreen = (Button)findViewById(R.id.accountBtnTopUpScreen);
        accountBtnTopUpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);

                startActivity(startIntent);
            }
        });

    }
}