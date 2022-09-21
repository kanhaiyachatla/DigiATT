package com.example.digiatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    EditText name,mail,pass,repass;
    Button signup,navlogin;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name=(EditText) findViewById(R.id.Name);
        mail=(EditText) findViewById(R.id.email);
        pass=(EditText) findViewById(R.id.password);
        repass=(EditText) findViewById(R.id.repassword);
        signup=(Button) findViewById(R.id.btnsignup);
        navlogin=(Button) findViewById(R.id.btn_navlogin);
        mAuth= FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createuser();
            }
        });

        navlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }

    private void createuser(){
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        String repassword = repass.getText().toString();

        if(TextUtils.isEmpty(email)){
            mail.setError("Email cannot be Empty");
            mail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            pass.setError("password cannot be Empty");
            pass.requestFocus();
        } else if (TextUtils.isEmpty(repassword)){
            repass.setError("Email cannot be Empty");
            repass.requestFocus();
        }else if (password.equals(repassword)){
            repass.setError("Passwords don't Match");
            pass.requestFocus();
            repass.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else{
                        Toast.makeText(SignupActivity.this, "Registration Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}