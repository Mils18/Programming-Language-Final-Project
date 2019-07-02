package com.example.insertandretrievefromfirebasenew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.Random;

//Add Product Class
public class AddProduct extends AppCompatActivity {
//    declares variables
    String name_s, shopName_s,prodname_s,desc_s,price_s,stock_s,imagePath_s,image_name,randomId_s,imagePath_gs_s;
    int price_i,stock_i;
    EditText prodNameEditText,descEditText,priceEditText,stockEditText;
    StorageReference imagePath_gs,storage;
    DatabaseReference ref,ref_p;
    ImageButton image;
    FirebaseDatabase database;
    Product product;
    private boolean valid = true;
    private int GalleryIntent = 2;
    Uri uri;


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
                    }
                    else{
                        Toast.makeText(AddProduct.this,"error1 ...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception ex){
            Toast.makeText(AddProduct.this,"error2", Toast.LENGTH_LONG).show();
        }
    }

    //function to generate random product ID
    private String generateRandomId(){
        final int randomNum = new Random().nextInt(90000) + 10000;
        String randomNum_s = Integer.toString(randomNum);
        randomId_s = "P_ID_"+randomNum_s;
        return randomId_s;
    }

    //upload set value into firebase
    private void uploadProduct(){
        ref_p.child(randomId_s).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddProduct.this,"Product Successfuly Uploaded",Toast.LENGTH_LONG).show();
                    Intent startIntent = new Intent(getApplicationContext(),ManageAccountSellAfter.class);
                    startIntent.putExtra("NAME_INTENT",name_s);
                    startActivity(startIntent);
                }else{
                    Toast.makeText(AddProduct.this,"failed to upload...!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //set all values to be uploaded
    private void setValue(){
        try{
            prodname_s = prodNameEditText.getText().toString();
            desc_s = descEditText.getText().toString();
            price_s = priceEditText.getText().toString();
            price_i = Integer.parseInt(price_s);
            stock_s = stockEditText.getText().toString();
            stock_i = Integer.parseInt(stock_s);
            imagePath_s = uri.toString();
            randomId_s = generateRandomId();
            product = new Product(prodname_s,desc_s,price_i,stock_i,shopName_s,image_name, imagePath_s, randomId_s);
            uploadProduct();
        } catch (Exception ex){
            Toast.makeText(AddProduct.this,"Everything should be filled correctly...!",Toast.LENGTH_LONG).show();
        }
    }

//    main function of Add Product class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        //declares all references, TextViews, EditTexts, and Buttons
        database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref_p = database.getReference("Products");
        prodNameEditText = (EditText) findViewById(R.id.prodNameEditText);
        descEditText = (EditText) findViewById(R.id.descriptionEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        stockEditText = (EditText) findViewById(R.id.stockEditText);
        storage = FirebaseStorage.getInstance().getReference();

        getIntentValue();

//      Buttons
        Button  uploadProductBtn = (Button)findViewById(R.id.addProdBtn);
        uploadProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValue();
            }
        });

        image = (ImageButton) findViewById(R.id.imageBtnAddProduct);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryIntent && resultCode == RESULT_OK){
            assert data != null;
            uri = data.getData();
            image.setImageURI(uri);
            assert uri != null;
            image_name = uri.getLastPathSegment();
            imagePath_gs = storage.child("ABC").child(image_name);

            imagePath_gs.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddProduct.this,"Uploaded.....",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this,"Not Uploaded.....",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
