package com.example.amrita.booksellingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.*;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout l3;
    EditText pass,email;
    Button reg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        l3=findViewById(R.id.l3);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        Animation uptodown=AnimationUtils.loadAnimation(this, R.anim.uptodown);
        l3.setAnimation(uptodown);
        email=findViewById(R.id.edittext4);
        pass=findViewById(R.id.edittext6);
        reg=findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        reg.setOnClickListener(this);
    }
    private void registerUser(){
        String emailId = email.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (emailId.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            email.setError("Enter a valid Email address");
            email.requestFocus();
            return;
        }
        if(password.length()<6){
            pass.setError("Min length of the password should be 6");
            pass.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(getApplicationContext(),"Registration Successsful",Toast.LENGTH_LONG).show();
                   Intent intent=new Intent(RegisterActivity.this,UserInfo.class);
                   startActivity(intent);
               }
               else{
                   if(task.getException() instanceof FirebaseAuthUserCollisionException){
                       Toast.makeText(getApplicationContext(),"You are already Registered",Toast.LENGTH_LONG).show();
                   }else{
                       Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                   }

               }
            }
        });
    }

    @Override
    public void onClick(View view) {
        registerUser();
    }
}
