package com.example.amrita.booksellingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amrita.booksellingapp.myClasses.BookDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BookAdapter.OnItemClickListener {
    private FirebaseFirestore db;
    private DrawerLayout drawerLayout;
    private com.getbase.floatingactionbutton.FloatingActionButton books,profile,wishlist;
    ImageView userimage;
    TextView username;
    FirebaseAuth mAuth;
    String userId;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    private ArrayList<BookDetails> bookDetailsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        NavigationView navigationView=findViewById(R.id.nav);
        View hview=navigationView.inflateHeaderView(R.layout.header);
        username=hview.findViewById(R.id.textview3);
        userimage=hview.findViewById(R.id.dp);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        db.collection("UserDetails").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String name=documentSnapshot.getString("name");
                username.setText(name);
                String imgUri = documentSnapshot.getString("img");
                Picasso.get().load(imgUri).into(userimage);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        books=findViewById(R.id.book);
        profile=findViewById(R.id.profile);
        wishlist=findViewById(R.id.wish);
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this,MyBookInfo.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView=findViewById(R.id.recycleview);
        //recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        bookDetailsArrayList=new ArrayList<>();
        bookAdapter=new BookAdapter(bookDetailsArrayList);
        recyclerView.setAdapter(bookAdapter);
        bookAdapter.setOnItemClickListener(StartActivity.this);
        db.collection("BookDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.filters:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FilterFragment()).commit();
                break;
            case R.id.st:

                break;
            case R.id.lo:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(getApplicationContext(),BookDescription.class);
        BookDetails clickedItem = bookDetailsArrayList.get(position);
        intent.putExtra("BOOK_NAME",clickedItem.getBookname());
        intent.putExtra("BOOK_IMAGE",clickedItem.getImg());
        intent.putExtra("AUTHOR_NAME",clickedItem.getAuthor());
        intent.putExtra("SEMESTER",clickedItem.getSem());
        intent.putExtra("CATEGORY",clickedItem.getCategory());
        intent.putExtra("PRICE",clickedItem.getMrp());
        intent.putExtra("ID",clickedItem.getUserId());
        startActivity(intent);
    }
}
