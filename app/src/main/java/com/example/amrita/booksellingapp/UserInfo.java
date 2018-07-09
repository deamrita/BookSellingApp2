package com.example.amrita.booksellingapp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amrita.booksellingapp.myClasses.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UserInfo extends AppCompatActivity implements View.OnClickListener{
    private final int PICK_IMAGE_REQUEST=199;
    EditText name,add,clg,pno;
    Button save,chpImg,svpImg;
    private FirebaseFirestore db;
    ImageView img;
    public Uri filepath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String profileImageUrl;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        img=findViewById(R.id.profileimage);
        name=findViewById(R.id.edittext5);
        add=findViewById(R.id.edittext7);
        clg=findViewById(R.id.edittext8);
        pno=findViewById(R.id.edittextpno);
        chpImg=findViewById(R.id.button8);
        svpImg=findViewById(R.id.button9);
        save=findViewById(R.id.button3);
        save.setOnClickListener(this);
        chpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        svpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });
    }
    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Toast.makeText(getApplicationContext(),"Dont forget to save your image",Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                filepath = data.getData();
                Picasso.get().load(filepath).centerCrop().fit().into(img);
            }

        }
        }
    public void saveImage(){
        if(filepath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            //final UserDetails userDetails=new UserDetails();
            final StorageReference ref=storageReference.child("ProfileImages/"+System.currentTimeMillis());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageUrl=uri.toString();
                            //userDetails.setImg(uri.toString());
                        }
                    });
                    //profileImageUrl=taskSnapshot.getUploadSessionUri().toString();
                    Toast.makeText(getApplicationContext(),"Profile image is uploaded",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(),"Profile image is not uploaded",Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }

    protected void saveUser(){
        String username = name.getText().toString().trim();
        String address = add.getText().toString().trim();
        String college = clg.getText().toString().trim();
        long phoneNo =   Long.parseLong(pno.getText().toString().trim());
        if (username.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            add.setError("Address is required");
            add.requestFocus();
            return;
        }
        if (college.isEmpty()) {
            clg.setError("College name is required");
            clg.requestFocus();
            return;
        }
        if(Long.toString(phoneNo).isEmpty()){
            pno.setError("College name is required");
            pno.requestFocus();
            return;
        }
        String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference dbInfo=db.collection("UserDetails");
        //UserDetails userDetails=new UserDetails(id,username,address,college,);
        UserDetails userDetails=new UserDetails();
        userDetails.setName(username);
        userDetails.setAdd(address);
        userDetails.setClg(college);
        userDetails.setImg(profileImageUrl);
        userDetails.setPno(phoneNo);
        userDetails.setId(id);
        dbInfo.document(id).set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
                Toast.makeText(getApplicationContext(),"User Info is uploaded",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(UserInfo.this,StartActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),"User Info is not uploaded",Toast.LENGTH_LONG).show();
            }
        });
        }



    @Override
    public void onClick(View view) {
        saveUser();
    }
}
