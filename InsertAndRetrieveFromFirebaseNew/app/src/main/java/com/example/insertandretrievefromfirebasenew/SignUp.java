package com.example.insertandretrievefromfirebasenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    User user;
    String name_s;
    String email_s;
    String password_s;
    String role_s;
    String shopName;
    int balance;
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    boolean isFound = true;


    private void createAccount(){
    ref.child(name_s).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(SignUp.this,"Account Successfuly created",Toast.LENGTH_LONG).show();
                Intent startIntent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(startIntent);
            }else{
                Toast.makeText(SignUp.this,"failed...!",Toast.LENGTH_LONG).show();
            }
        }
    });
    }

private void ExistenceValidationAndCreateAccount(){
    try{
        ref.child(name_s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_validation = dataSnapshot.getValue(User.class);
                if (user_validation == null){
                    isFound = false;
                    System.out.println("CreateAccount");
                    createAccount();
                }
//                System.out.println("Num"+num);
                if(isFound){
                    System.out.println("Found");
                    Toast.makeText(SignUp.this,"Username is taken",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }catch (Exception ex){
        Toast.makeText(SignUp.this,"Error", Toast.LENGTH_LONG).show();
    }
}


    private void getValue(){

        nameEditText = (EditText) findViewById(R.id.nameSignUpEditText);
        emailEditText = (EditText) findViewById(R.id.emailSignUpEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignUpEditTExt);

        name_s = nameEditText.getText().toString();
        email_s = emailEditText.getText().toString();
        password_s = passwordEditText.getText().toString();
        role_s = "Regular";
        shopName = "invalid";
        balance = 0;
        user = new User(name_s,email_s,password_s,role_s,shopName, balance);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");

        Button registerBtn = (Button) findViewById(R.id.signUpBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getValue();
            ExistenceValidationAndCreateAccount();
            }
        });


        Button goToSignInBtn = (Button)findViewById(R.id.goToSignInBtn);
        goToSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(),SignIn.class);
                startActivity(startIntent);
            }
        });
    }
}

