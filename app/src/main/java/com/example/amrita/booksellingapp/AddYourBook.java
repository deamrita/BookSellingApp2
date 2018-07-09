package com.example.amrita.booksellingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.amrita.booksellingapp.myClasses.BookDetails;
import com.example.amrita.booksellingapp.myClasses.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class AddYourBook extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    private final int PICK_IMAGE_REQUEST=99;
    private Spinner sp;
    EditText bookname,author,sem,mrp;
    Button chImg,svImg,save;
    private FirebaseFirestore db;
    ImageView img;
    private Uri filepath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String bookImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_book);
        sp = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        img=findViewById(R.id.bookimage);
        bookname = findViewById(R.id.bookName);
        author = findViewById(R.id.bookAuthor);
        sem = findViewById(R.id.sem);
        mrp = findViewById(R.id.price);
        chImg=findViewById(R.id.button5);
        svImg=findViewById(R.id.button6);
        save=findViewById(R.id.button4);
        save.setOnClickListener(this);
        chImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        svImg.setOnClickListener(new View.OnClickListener() {
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

    @Override
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

            final StorageReference ref=storageReference.child("BookImages/"+UUID.randomUUID().toString());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            bookImageUrl=uri.toString();
                            //userDetails.setImg(uri.toString());
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Book image is uploaded",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(),"Book image is not uploaded",Toast.LENGTH_LONG).show();
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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String category = adapterView.getItemAtPosition(i).toString();
        if(category.equals("Select the category")){
            Toast.makeText(AddYourBook.this, "give your book information", Toast.LENGTH_LONG).show();
        }
        else {
           // Toast.makeText(AddYourBook.this, "You have selected " + category, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void saveBook(){
        String book = bookname.getText().toString().trim();
        String authorname = author.getText().toString().trim();
        String semester = sem.getText().toString().trim();
        int price=Integer.parseInt(mrp.getText().toString());
        String bookcategory=sp.getSelectedItem().toString();
        //String bookimage=filepath.toString();
        if (book.isEmpty()) {
            bookname.setError("Book name is required");
            bookname.requestFocus();
            return;
        }

        if (authorname.isEmpty()) {
            author.setError("Author name is required");
            author.requestFocus();
            return;
        }
        if (semester.isEmpty()) {
            sem.setError("Semester is required");
            sem.requestFocus();
            return;
        }
        if (Integer.toString(price).isEmpty()) {
            mrp.setError("Book price is required");
            mrp.requestFocus();
            return;
        }
        String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference bInfo =db.collection("BookDetails");
        BookDetails bookDetails=new BookDetails();
        bookDetails.setBookname(book);
        bookDetails.setAuthor(authorname);
        bookDetails.setSem(semester);
        bookDetails.setMrp(price);
        bookDetails.setCategory(bookcategory);
        bookDetails.setImg(bookImageUrl);
        bookDetails.setUserId(userid);
        bInfo.add(bookDetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
                Toast.makeText(AddYourBook.this,"book info is saved",Toast.LENGTH_LONG).show();
                Intent intent =new Intent(AddYourBook.this,StartActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddYourBook.this,"book info is not saved",Toast.LENGTH_LONG).show();
            }
        });


    }



    @Override
    public void onClick(View view) {
        saveBook();
    }
}
