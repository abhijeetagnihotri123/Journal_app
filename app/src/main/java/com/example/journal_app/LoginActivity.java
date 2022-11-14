package com.example.journal_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journal_app.JournalApi1.JournalApi1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalApi;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button createAcctButton;

    private AutoCompleteTextView emailAddress;
    private EditText password;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.email_sign_in);
        createAcctButton = findViewById(R.id.create_acct);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);

        createAcctButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,CreateAccountActivity1.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String email = emailAddress.getText().toString().trim();
            String Password = password.getText().toString().trim();
            loginEmailPasswordUser(email,Password);
        });
    }

    private void loginEmailPasswordUser(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    String currentUserId = user.getUid();
                    collectionReference.whereEqualTo("userId",currentUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null)
                            {
                                return;
                            }
                            assert value != null;
                            if(!value.isEmpty())
                            {
                                for(QueryDocumentSnapshot snapshot : value)
                                {
                                    JournalApi1 journalApi1 = JournalApi1.getInstance();
                                    journalApi1.setUserId(snapshot.getString("userId"));
                                    journalApi1.setUsername(snapshot.getString("username"));
                                    startActivity(new Intent(LoginActivity.this,PostJournalActivity.class));
                                }
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Incorrect UserId or Password",Toast.LENGTH_LONG).show();
        }
    }

}