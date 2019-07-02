package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;

public class ProductDetail2 extends AppCompatActivity {
    ImageView imageViewProdDetail2;
    TextView prodNameTV,prodPriceTV,stockLeftTextViewProdDetail2;
    EditText prodQuantityEditText;
    String prodName_s,name_s,prodID_s,prodPrice_s,invoiceNumber_s,prodShopName_s,prodQuantity_s,imageURI_s;
    int prodQuantity_i,prodPrice_i,prodStockLeft_i;
    boolean stockIsSuffient = false;
    Cart cart;
    Uri imageURI;

    FirebaseDatabase database;
    DatabaseReference ref,ref_p,ref_c;

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
            ref_p.child(prodID_s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product prod = dataSnapshot.getValue(Product.class);
                    if(prod!=null){
                        prodName_s = prod.getProdName();
                        prodPrice_i = prod.getPrice();
                        prodStockLeft_i = prod.getStock();
                        prodShopName_s = prod.getShopName();
                        prodID_s = prod.getproductID();
                        prodNameTV.setText(prodName_s);
                        imageURI_s = prod.getImageURI();
                        stockLeftTextViewProdDetail2.setText("Stock Left : "+prod.getStock());
                        prodPriceTV.setText("Rp. "+prodPrice_i);

                        imageURI = imageURI.parse(prod.getImageURI());
                        Picasso.with(ProductDetail2.this).load(imageURI).placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher).into(imageViewProdDetail2, new com.squareup.picasso.Callback() {
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
                        Toast.makeText(ProductDetail2.this,"Invalid Product1 ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(ProductDetail2.this,"Invalid Product2 ...", Toast.LENGTH_LONG).show();
        }
    }

//    private void uploadToDataBase(Cart cart){
//
//        try{
//            System.out.println(cart.getCustomerName());
//            ref_c.child(cart.getProdName()).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Toast.makeText(ProductDetail2.this,"Product Successfuly Added to cart",Toast.LENGTH_LONG).show();
//                        Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
//                        startIntent.putExtra("NAME_INTENT",name_s);
//                        startActivity(startIntent);
//                    }else{
//                        Toast.makeText(ProductDetail2.this,"failed to add...!",Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

    private void uploadToDatabase(Cart c){
        ref_c.child(c.getBookingCode()).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProductDetail2.this,"Added To Cart...", Toast.LENGTH_LONG).show();

                    Intent startIntent = new Intent(getApplicationContext(),Cart_Screen.class);
                    startIntent.putExtra("NAME_INTENT",name_s);
                    startActivity(startIntent);

                }else{
                    Toast.makeText(ProductDetail2.this,"Error to upload to database",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean quantityCheck(){
        System.out.println("Q check prod detail"+prodQuantity_i);
        System.out.println("stock check prod detail"+prodStockLeft_i);
        if (prodQuantity_i <= prodStockLeft_i){
            return true;
        }
        return false;
    }
    private String generateRandomBookingCode(){
        final int randomNum = new Random().nextInt(90000) + 10000;
        String randomNum_s = Integer.toString(randomNum);
        String bookingCode_s = "BOOK_"+randomNum_s;
        return bookingCode_s;
    }
    private void setValue() {
        String invoiceNumber = "INVALID_INVOICE";
        String customerName = name_s;
        String shopName = prodShopName_s;
        String prodID = prodID_s;
        String prodName = prodName_s;
        String status = "UNPAID";
        String bookingCode = generateRandomBookingCode();
        String imageURI = imageURI_s;
        int prodPrice = prodPrice_i;
        int quantity = prodQuantity_i;

        stockIsSuffient = quantityCheck();
        if (stockIsSuffient) {
            System.out.println("sufficientProdDetail2");
            cart = new Cart(invoiceNumber, customerName, shopName, prodID, prodName, status, bookingCode, imageURI, prodPrice, quantity);
            uploadToDatabase(cart);
        } else {
//            System.out.println("Insufficient Stock");
            Toast.makeText(ProductDetail2.this, "Insuffient Stock", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail2);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref_p = FirebaseDatabase.getInstance().getReference().child("Products");
        ref_c = FirebaseDatabase.getInstance().getReference().child("Cart");

        prodNameTV = (TextView)findViewById(R.id.prodNameTextViewProdDetail2);
        stockLeftTextViewProdDetail2 = (TextView)findViewById(R.id.stockLeftTextViewProdDetail2);
        prodPriceTV = (TextView)findViewById(R.id.priceTextViewProdDetail2);
        prodQuantityEditText = (EditText)findViewById(R.id.quantityEditTextProdDetail2);
        imageViewProdDetail2 = (ImageView)findViewById(R.id.imageViewProdDetail2);

        getIntentValue();


        Button addToCartBtnProdDetail2 = (Button)findViewById(R.id.addToCartBtnProdDetail2);
        addToCartBtnProdDetail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prodQuantity_s = prodQuantityEditText.getText().toString();
                prodQuantity_i = Integer.parseInt(prodQuantity_s);
                setValue();

            }
        });

    }

}
