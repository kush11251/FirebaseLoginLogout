package com.example.firebaseloginlogout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegister;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName           = findViewById(R.id.fullName);
        mEmail              = findViewById(R.id.email);
        mPassword           = findViewById(R.id.password);
        mPhone              = findViewById(R.id.phone);

        mRegister           = findViewById(R.id.login);
        mLoginBtn           = findViewById(R.id.notregister);

        fAuth               = FirebaseAuth.getInstance();
        fStore              = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String pword = mPassword.getText().toString().trim();
                String fname = mFullName.getText().toString();
                String phone = mPhone.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pword)) {
                    mEmail.setError("Email/Password is Required.");
                    return;
                }

                if (pword.length() < 6) {
                    mPassword.setError("Password must be more than 6 Character!!!!");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,pword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"User Created.",
                                            Toast.LENGTH_SHORT).show();

                                    userID = fAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection(
                                            "users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Full Name",fname);
                                    user.put("Email",email);
                                    user.put("Phone",phone);

                                    documentReference.set(user).addOnSuccessListener
                                            (new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("Info","onSuccess : user Profile is " +
                                                    "created for " + userID);
                                        }
                                    });

                                    documentReference.set(user).addOnFailureListener
                                            (new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.i("Info","onFailure : "
                                                    + e.toString());
                                                }
                                            });

                                    startActivity(new Intent(getApplicationContext(),
                                            MainActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(),"Error!!! " +
                                            task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Not Allowed!!!!",Toast.LENGTH_SHORT).show();
    }
}