package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;

public class ProductDetail extends AppCompatActivity {
    ImageView imageViewProdDetail;
    TextView prodNameTV,prodPriceTV,prodShopNameTV,descTextViewProductDetail;
    String prodName_s,name_s,prodImage_s,prodID_s,prodShopName_s,prodDesc_s,userShopName_s;
    Button addToCartBtnProdDetail;
    Uri imageURI;
    int prodPrice_i;
    FirebaseDatabase database;
    DatabaseReference ref,ref_p;
    StorageReference storageRef;
    FirebaseStorage storage;

    protected void getIntentValue(){
        if(getIntent().hasExtra("NAME_INTENT")){
            name_s = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_INTENT");
            System.out.println("name_s"+name_s);
        }
        if(getIntent().hasExtra("PRODID_INTENT")){
            prodID_s = Objects.requireNonNull(getIntent().getExtras()).getString("PRODID_INTENT");
            System.out.println("prodID_s"+prodID_s);
        }
        try{
            ref.child(name_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null){
                        userShopName_s = user.getShopName();
                        System.out.println("userShopName_s "+userShopName_s);
                    }
                    else{
                        Toast.makeText(ProductDetail.this,"Invalid Name ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(ProductDetail.this,"Invalid Product2 ...", Toast.LENGTH_LONG).show();
        }
        try{
            ref_p.child(prodID_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product prod = dataSnapshot.getValue(Product.class);
                    if(prod!=null){
                        prodName_s = prod.getProdName();
                        prodPrice_i = prod.getPrice();
                        prodShopName_s = prod.getShopName();
                        prodDesc_s = prod.getDescription();
                        System.out.println("prodImage_s "+prodImage_s);
                        prodNameTV.setText(prodName_s);
                        prodPriceTV.setText("Rp. "+prodPrice_i);
                        prodShopNameTV.setText("Seller : "+prodShopName_s);
                        descTextViewProductDetail.setText(prodDesc_s);
                        if(!prod.getShopName().equals(userShopName_s)) {
                            addToCartBtnProdDetail.setVisibility(View.VISIBLE);
                        }

                        imageURI = imageURI.parse(prod.getImageURI());
                        Picasso.with(ProductDetail.this).load(imageURI).placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher).into(imageViewProdDetail, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                 System.out.println("SUCCESS LOAD IMAGE");

                            }

                            @Override
                            public void onError() {
                                System.out.println("ERROR LOAD IMAGE");

                            }
                        });
                    }
                    else{
                        Toast.makeText(ProductDetail.this,"Invalid Product1 ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(ProductDetail.this,"Invalid Product2 ...", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref_p = FirebaseDatabase.getInstance().getReference().child("Products");
        storage = FirebaseStorage.getInstance();


        prodNameTV = (TextView)findViewById(R.id.prodNameTextViewProdDetail);
        prodPriceTV = (TextView)findViewById(R.id.priceTextViewProdDetail);
        prodShopNameTV = (TextView)findViewById(R.id.shopNameTextViewProdDetail);
        descTextViewProductDetail = (TextView)findViewById(R.id.descTextViewProductDetail);
        imageViewProdDetail = (ImageView)findViewById(R.id.imageViewProdDetail);
        addToCartBtnProdDetail = (Button)findViewById(R.id.addToCartBtnProdDetail);

        getIntentValue();


        addToCartBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetail.this,"addToCartBtnProdDetail", Toast.LENGTH_LONG).show();
                Intent startIntent = new Intent(getApplicationContext(),ProductDetail2.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startIntent.putExtra("PRODID_INTENT",prodID_s);
                startActivity(startIntent);
            }
        });

        Button homeBtnProdDetail = (Button)findViewById(R.id.homeBtnProdDetail);
        homeBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetail.this,"homeBtnProdDetail", Toast.LENGTH_LONG).show();
                Intent startIntent = new Intent(getApplicationContext(),Home.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button cartBtnProdDetail = (Button)findViewById(R.id.cartBtnProdDetail);
        cartBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetail.this,"cartBtnProdDetail", Toast.LENGTH_LONG).show();
                Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });

        Button accountBtnProdDetail = (Button)findViewById(R.id.accountBtnProdDetail);
        accountBtnProdDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetail.this,"accountBtnProdDetail", Toast.LENGTH_LONG).show();
                Intent startIntent = new Intent(getApplicationContext(),ManageAccountBuy.class);
                startIntent.putExtra("NAME_INTENT",name_s);
                startActivity(startIntent);
            }
        });
    }

}
