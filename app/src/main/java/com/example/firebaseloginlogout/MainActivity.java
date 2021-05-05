package com.example.firebaseloginlogout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView fullname,email,phone;
    String userID;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullname    = findViewById(R.id.profileNameTextView);
        email       = findViewById(R.id.profileEmailTextView);
        phone       = findViewById(R.id.profilePhoneTextView);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();

        userID      = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users")
                .document(userID);

        documentReference.addSnapshotListener(this,
                new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                phone.setText(value.getString("Phone"));
                fullname.setText(value.getString("Full Name"));
                email.setText(value.getString("Email"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Not Allowed!!!!",Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}