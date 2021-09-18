package com.scanne;



import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
  private ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();

 registerUser = (Button) findViewById(R.id.registerUser);
 registerUser.setOnClickListener(this);

 editTextFullName = (EditText) findViewById(R.id.editTextTextPersonName);
 editTextAge =(EditText) findViewById(R.id.editTextTextPersonName2);
 editTextEmail=(EditText) findViewById(R.id.editTextTextEmailAddress);
 editTextPassword=(EditText) findViewById(R.id.editTextTextPassword);

 progressBar = (ProgressBar) findViewById(R.id.loading);

    }

    @Override
    public void onClick(View v) {
switch(v.getId())
{
    case R.id.titleMain:
        startActivity(new Intent(this, LoginActivity.class));
        break;
    case R.id.registerUser:
        registUser();
        break;
}
    }
    private void registUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullname = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
                     if(fullname.isEmpty())
                     {
                         editTextFullName.setError("Full name is required!");
                         editTextFullName.requestFocus();
                         return;
                     }
                     else if(age.isEmpty())
                     {
                         editTextAge.setError("Age is required!");
                         editTextAge.requestFocus();
                         return;
                     }
                     else if (password.isEmpty())
                     {
                         editTextPassword.setError("Password is required!");
                         editTextPassword.requestFocus();
                         return;
                     }
                     else if (password.length()<6)
                     {
                         editTextPassword.setError("Minimal password length should ne 6 characters!");
                         editTextPassword.requestFocus();
                         return;
                     }
                     else if (email.isEmpty())
                     {
                         editTextEmail.setError("Email is required!");
                         editTextEmail.requestFocus();
                         return;
                     }
                     else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                     {
                         editTextEmail.setError("Please provide valid email!");
                         editTextEmail.requestFocus();
                         return;
                     }

            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        User user = new User(fullname, age, email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }else
                                {
                                    Toast.makeText(RegisterUser.this, "Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
    }
}
