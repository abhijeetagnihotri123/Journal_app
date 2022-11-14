package com.example.journal_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journal_app.JournalApi1.JournalApi1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.JournalApi;

public class CreateAccountActivity1 extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private EditText userNameEditText;
    private Button createAcctButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account1);

        firebaseAuth = FirebaseAuth.getInstance();

        createAcctButton = findViewById(R.id.create_acct);
        progressBar = findViewById(R.id.create_acct_progress);
        emailEditText = findViewById(R.id.email_account);
        passwordEditText = findViewById(R.id.password_account);
        userNameEditText = findViewById(R.id.username_account);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null)
                {
                    //will update
                }else{
                    // will update
                }
            }
        };

        createAcctButton.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(emailEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString()) && !TextUtils.isEmpty(userNameEditText.getText().toString())) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String username = userNameEditText.getText().toString().trim();
                createUserEmailAccount(email, password, username);
            }
            else
            {
                Toast.makeText(CreateAccountActivity1.this,"Non-empty fields not allowed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createUserEmailAccount(String email,String password,String username)
    {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username))
        {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(CreateAccountActivity1.this,"HERE1",Toast.LENGTH_LONG).show();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(CreateAccountActivity1.this,"HERE2",Toast.LENGTH_LONG).show();
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        String currentUserId = currentUser.getUid();
                        Map<String,String> userObj = new HashMap<>();
                        userObj.put("userId",currentUserId);
                        userObj.put("username",username);
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(Objects.requireNonNull(task.getResult().exists()))
                                        {
                                            String name = task.getResult().getString("username");
                                            JournalApi1 journalApi1 = JournalApi1.getInstance();
                                            journalApi1.setUsername(name);
                                            journalApi1.setUserId(currentUserId);

                                            Intent intent = new Intent(CreateAccountActivity1.this,PostJournalActivity.class);
                                            intent.putExtra("username",name);
                                            intent.putExtra("userId",currentUserId);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateAccountActivity1.this,"Problem "+e.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(CreateAccountActivity1.this,"No User "+e.toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}