package com.example.amrita.booksellingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout l2;
    EditText name;
    EditText pass;
    Button btn;
    TextView tv;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l2=findViewById(R.id.l2);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        Animation uptodown=AnimationUtils.loadAnimation(this, R.anim.uptodown);
        l2.setAnimation(uptodown);
        name=(EditText)findViewById(R.id.edittext2);
        pass=(EditText)findViewById(R.id.edittext3);
        btn=findViewById(R.id.button1);
        tv=findViewById(R.id.textview2);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,StartActivity.class);
            startActivity(intent);
        }
        btn.setOnClickListener(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void userLogin(){
        String emailId = name.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (emailId.isEmpty()) {
            name.setError("Email is required");
            name.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            name.setError("Enter a valid Email address");
            name.requestFocus();
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
        mAuth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent=new Intent(MainActivity.this,StartActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        userLogin();
    }
}
