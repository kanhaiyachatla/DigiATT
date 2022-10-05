package com.example.digiatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnlogout,btnattend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();

        btnlogout=(Button) findViewById(R.id.btn_logout);
        btnattend=(Button)findViewById(R.id.btn_attend);

        btnattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        BiometricPrompt.PromptInfo promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Please verify").setDescription("User Authentication is required to proceed").setNegativeButtonText("Cancel").build();
                        getPrompt().authenticate(promptInfo);
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }

        private BiometricPrompt getPrompt(){
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    notifyUser(errString.toString());
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    notifyUser("Authentication Succeeded!!");
                    startActivity(new Intent(getApplicationContext(),FingerprintActivity.class));
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    notifyUser("Authentication Failed");
                }
            };

            BiometricPrompt biometricPrompt = new BiometricPrompt(this,executor,callback);
            return biometricPrompt;
        }

        private void notifyUser(String message){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

}