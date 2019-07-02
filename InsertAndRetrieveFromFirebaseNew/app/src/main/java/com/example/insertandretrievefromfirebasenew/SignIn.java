package com.example.insertandretrievefromfirebasenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    DatabaseReference ref;
    private User user;
    private String password_s;
    private String name_s;
    private EditText nameEditText;
    private EditText passwordEditText;


    private void signIn(){
        try{
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null && password_s.equals(user.getPassword()) ){
//                        if(user.getRole().equals("Regular")){
                        Intent startIntent = new Intent(getApplicationContext(), Home.class);
                        startIntent.putExtra("NAME_INTENT",name_s);
//                        startIntent.putExtra("USER_DATA",user);

                        startActivity(startIntent);
//                        }
//                        else if(user.getRole().equals("Business")){
//                            Intent startIntent = new Intent(getApplicationContext(), BusinessAccMainMenu.class);
//                            startIntent.putExtra("INTENT_BUSINESSACCMAINMENU_NAME",name_s);
//                            startIntent.putExtra("INTENT_BUSINESSACCMAINMENU_SHOPNAME",user.getShopName());
//                            startActivity(startIntent);
//                        }
                    }
                    else{
                        Toast.makeText(SignIn.this,"Incorrect Username or Password ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignIn.this,"Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ex){
            Toast.makeText(SignIn.this,"There is no Account Created Yet", Toast.LENGTH_LONG).show();
        }
    }

    private void getValue(){
        nameEditText = (EditText) findViewById(R.id.nameSignInEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignInEditText);

        name_s =  nameEditText.getText().toString();
        password_s = passwordEditText.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValue();
                signIn();
            }
        });

        Button goToSignUpBtn = (Button)findViewById(R.id.goToSignUpBtn);
        goToSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(startIntent);
            }
        });

    }
}