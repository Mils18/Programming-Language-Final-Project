package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Upgrade extends AppCompatActivity {
    EditText shopNameEditText;
//    ImageButton imageButton;
    String shopName_s;
    Button done;
    FirebaseDatabase database;
    DatabaseReference ref;
    boolean isFound = true;
//    StorageReference imagePath;
//    private Merchant merchant;
//    private int gallery_req_code = 2;
    String name_s;

    private void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
        }
    }
    private void uploadToDatabase(User user_validation){
        ref.child(name_s).setValue(user_validation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Upgrade.this, "You have successfuly upgraded your account to Business Account", Toast.LENGTH_LONG).show();
                    Intent startIntent = new Intent(getApplicationContext(), ManageAccountSellAfter.class);
                    startIntent.putExtra("NAME_INTENT",name_s);
                    startIntent.putExtra("SHOPNAME_INTENT",shopName_s);
                    startActivity(startIntent);
                }else{
                    Toast.makeText(Upgrade.this,"Error to upload to database",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateData(){
        try{
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user_validation = dataSnapshot.getValue(User.class);
                    System.out.println("00");
//                num = num + 1;
                    assert user_validation != null;
                    if(user_validation.getName().equals(name_s)){
                        user_validation.setRole("Business");
                        user_validation.setShopName(shopName_s);
                        uploadToDatabase(user_validation);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(Upgrade.this,"Error to Update Data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade);

        shopNameEditText = (EditText) findViewById(R.id.shopNameEditText);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        getIntentValue();

        done = (Button) findViewById(R.id.doneBtn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            shopName_s = shopNameEditText.getText().toString();
            updateData();
            }
        });
    }
}


