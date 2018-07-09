package com.example.amrita.booksellingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class BookDescription extends AppCompatActivity {
    ImageView i1;
    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    private FirebaseFirestore db1,db2;
    private FloatingActionButton phoneCall;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);
        Intent intent=getIntent();
        String imgUrl=intent.getStringExtra("BOOK_IMAGE");
        String bookName=intent.getStringExtra("BOOK_NAME");
        String authorName=intent.getStringExtra("AUTHOR_NAME");
        String sem=intent.getStringExtra("SEMESTER");
        String category=intent.getStringExtra("CATEGORY");
        final String userId=intent.getStringExtra("ID");
        int price=intent.getIntExtra("PRICE",0);
        db1=FirebaseFirestore.getInstance();
        db2=FirebaseFirestore.getInstance();
        i1=findViewById(R.id.imageview2);
        t1=findViewById(R.id.bname);
        t2=findViewById(R.id.aname);
        t3=findViewById(R.id.semester);
        t4=findViewById(R.id.category);
        t5=findViewById(R.id.price);
        t6=findViewById(R.id.oname);
        t7=findViewById(R.id.oclg);
        t8=findViewById(R.id.olocation);
        Picasso.get().load(imgUrl).fit().centerCrop().into(i1);
        t1.setText("Book Name: "+bookName);
        t2.setText("Author Name: "+authorName);
        t3.setText("Sem: "+sem);
        t4.setText("Category: "+category);
        t5.setText("price: "+price);
        phoneCall=findViewById(R.id.makeCall);
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db1.collection("UserDetails").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Long number=documentSnapshot.getLong("pno");
                        //Toast.makeText(getApplicationContext(),"number is "+ number,Toast.LENGTH_LONG).show();
                        if(ContextCompat.checkSelfPermission(BookDescription.this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                           ActivityCompat.requestPermissions(BookDescription.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                       }else{
                           String dial ="tel:" + number;
                           startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
                      }
                   }
               });
           }
        });
        db2.collection("UserDetails").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String name=documentSnapshot.getString("name");
                t6.setText("Owner's name: "+name);
                String collegeName=documentSnapshot.getString("clg");
                t7.setText("Owner's college name: "+collegeName);
                String City=documentSnapshot.getString("add");
                t8.setText("Owner's Location: "+City);

            }
        });
    }



}
