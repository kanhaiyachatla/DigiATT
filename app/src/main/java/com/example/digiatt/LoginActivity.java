package com.example.digiatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    boolean isBackPressedOnce = false;
    EditText mail,pass;
    Button login,navsignup;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mail=(EditText) findViewById(R.id.mail1);
        pass=(EditText) findViewById(R.id.pass1);
        login=(Button) findViewById(R.id.button);
        navsignup= (Button) findViewById(R.id.btn_navsignup);
        mAuth= FirebaseAuth.getInstance();


        navsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isBackPressedOnce){
        super.onBackPressed();
        return;
        }
        Toast.makeText(this, "Press again to Exit.", Toast.LENGTH_SHORT).show();
        isBackPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressedOnce = false;
            }
        },2000);

    }

    private void loginuser(){
        String email = mail.getText().toString();
        String password = pass.getText().toString();

        if(TextUtils.isEmpty(email)){
            mail.setError("Email cannot be Empty");
            mail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            pass.setError("password cannot be Empty");
            pass.requestFocus();
        }else{
            login.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if (mAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(LoginActivity.this, "User Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Please verify your Email first", Toast.LENGTH_SHORT).show();
                            login.setEnabled(true);

                        }
                    }else{
                        String msg = task.getException().getMessage();
                        if (msg.contains("no user record")){
                            Toast.makeText(LoginActivity.this, "E-mail Address does not Exist", Toast.LENGTH_SHORT).show();
                        }else if (msg.contains("password is invalid")) {
                            Toast.makeText(LoginActivity.this, "Password is Invalid", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                        login.setEnabled(true);
                    }
                }
            });
        }
    }

}