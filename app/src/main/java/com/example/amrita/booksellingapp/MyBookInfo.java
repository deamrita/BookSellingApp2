package com.example.amrita.booksellingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amrita.booksellingapp.myClasses.BookDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyBookInfo extends AppCompatActivity implements BookAdapter.OnItemClickListener {
    private FloatingActionButton addbook;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    private ArrayList<BookDetails> bookDetailsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_info);
        recyclerView=findViewById(R.id.rview);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        bookDetailsArrayList=new ArrayList<>();
        bookAdapter=new BookAdapter(bookDetailsArrayList);
        recyclerView.setAdapter(bookAdapter);
        bookAdapter.setOnItemClickListener(MyBookInfo.this);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        db.collection("BookDetails").whereEqualTo("userId",mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        BookDetails bd=d.toObject(BookDetails.class);
                        bookDetailsArrayList.add(bd);
                        }
                    bookAdapter.notifyDataSetChanged();
                }
            }
        });

        addbook=findViewById(R.id.addBook);
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyBookInfo.this,AddYourBook.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}
