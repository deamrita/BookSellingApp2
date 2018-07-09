package com.example.amrita.booksellingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    ImageView img;
    TextView t1,t2,t3;
    FirebaseAuth mAuth;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        img =findViewById(R.id.profileimage);
        t1=findViewById(R.id.textview4);
        t2=findViewById(R.id.textview5);
        t3=findViewById(R.id.textview6);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        db.collection("UserDetails").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String name=documentSnapshot.getString("name");
                t1.setText("User's name: "+name);
                String collegeName=documentSnapshot.getString("clg");
                t2.setText("User's college name: "+collegeName);
                String City=documentSnapshot.getString("add");
                t3.setText("User's Location: "+City);
                String imgUri=documentSnapshot.getString("img");
                Picasso.get().load(imgUri).into(img);
            }
        });
    }
}
