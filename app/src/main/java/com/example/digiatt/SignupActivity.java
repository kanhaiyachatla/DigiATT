package com.example.digiatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText name,mail,pass,repass,Userid;
    Button signup,navlogin;
    FirebaseAuth mAuth;
    FirebaseFirestore database;
    RadioGroup group;
    RadioButton student,teacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name=(EditText) findViewById(R.id.Name);
        Userid = (EditText) findViewById(R.id.UID);
        mail=(EditText) findViewById(R.id.email);
        pass=(EditText) findViewById(R.id.password);
        repass=(EditText) findViewById(R.id.repassword);
        signup=(Button) findViewById(R.id.btnsignup);
        navlogin=(Button) findViewById(R.id.btn_navlogin);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        teacher = (RadioButton) findViewById(R.id.teacherbtn);
        student = (RadioButton) findViewById(R.id.studentbtn);
        mAuth= FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (group.getCheckedRadioButtonId() != -1){
                    if (teacher.isChecked()){
                        createteacher();

                    }else{
                        createstudent();

                    }
                }else{
                    Toast.makeText(SignupActivity.this, "Please select one option", Toast.LENGTH_SHORT).show();

                }

            }
        });

        navlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }


    private void createstudent() {
        String name1 = name.getText().toString();
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        String repassword = repass.getText().toString();

        if (TextUtils.isEmpty(name1)) {
            name.setError("Name cannot be Empty");
            name.requestFocus();
        }  else if(TextUtils.isEmpty(email)){
            mail.setError("Email cannot be Empty");
            mail.requestFocus();

        } else if (TextUtils.isEmpty(password)) {
            pass.setError("password cannot be Empty");
            pass.requestFocus();

        }else if (!isValidPassword(password)) {
            pass.setError("Password too weak");
        } else if (TextUtils.isEmpty(repassword)){
            repass.setError("Email cannot be Empty");
            repass.requestFocus();

        }else if(!(password.equals(repassword))){
            repass.setError("Passwords don't Match");
            pass.requestFocus();
            repass.requestFocus();
        }else{
            signup.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String UID = Userid.getText().toString();
                        DocumentReference ref = database.collection("Users").document("Students").collection("Students").document(UID);
                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Userid.setError("UID already exists");
                                        Userid.requestFocus();
                                    }else{
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("name", name1);
                                        user.put("email", email);
                                        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            Toast.makeText(SignupActivity.this, "Registration Successful. Please check your email to verify Account", Toast.LENGTH_SHORT).show();
                                                            name.setText("");
                                                            mail.setText("");
                                                            pass.setText("");
                                                            repass.setText("");
                                                            signup.setEnabled(true);

                                                        }else{
                                                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }
                        });




                    }else{
                        signup.setEnabled(true);
                        String error = task.getException().getMessage();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }
            });
        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    private void createteacher() {
        String name1 = name.getText().toString();
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        String repassword = repass.getText().toString();

        if (TextUtils.isEmpty(name1)) {
            name.setError("Name cannot be Empty");
            name.requestFocus();
        }  else if(TextUtils.isEmpty(email)){
            mail.setError("Email cannot be Empty");
            mail.requestFocus();

        } else if (TextUtils.isEmpty(password)) {
            pass.setError("password cannot be Empty");
            pass.requestFocus();

        }else if (!isValidPassword(password)) {
            pass.setError("Password too weak");
        } else if (TextUtils.isEmpty(repassword)){
            repass.setError("Email cannot be Empty");
            repass.requestFocus();

        }else if(!(password.equals(repassword))){
            repass.setError("Passwords don't Match");
            pass.requestFocus();
            repass.requestFocus();
        }else{
            signup.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String UID = Userid.getText().toString();
                        DocumentReference ref = database.collection("Users").document("Teachers").collection("Teachers").document(UID);
                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Userid.setError("UID already exists");
                                        Userid.requestFocus();
                                    }else{
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("name", name1);
                                        user.put("email", email);
                                        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            Toast.makeText(SignupActivity.this, "Registration Successful. Please check your email to verify Account", Toast.LENGTH_SHORT).show();
                                                            name.setText("");
                                                            mail.setText("");
                                                            pass.setText("");
                                                            repass.setText("");
                                                            signup.setEnabled(true);

                                                        }else{
                                                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }
                        });




                    }else{
                        signup.setEnabled(true);
                        String error = task.getException().getMessage();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }
            });
        }
    }

}