package com.scanne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity //implements View.OnClickListener
         {
   // private TextView register;
    private EditText editTextEmail, editTextPassword;
   //          private Button signIn;
             private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // register = (TextView) findViewById(R.id.regist);
      //  register.setOnClickListener(this);
      //  signIn=(Button) findViewById(R.id.login);
  //      signIn.setOnClickListener(this);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword =(EditText)findViewById(R.id.password);
   progressBar = (ProgressBar) findViewById(R.id.loading);
    mAuth = FirebaseAuth.getInstance();
    }
 /*   @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.regist:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }

    }*/
    private void userLogin()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(email.isEmpty())
        {
            editTextEmail.setError("Потрібна електронна пошта ");
            editTextEmail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Будь ласка, надайте дійсний електронну пошту!");
            editTextEmail.requestFocus();
            return;
        }
        else if (password.isEmpty())
        {
            editTextPassword.setError(" Необхіден пароль!");
            editTextPassword.requestFocus();
            return;
        }
        else if (password.length()<6)
        {
            editTextPassword.setError("Мінімальна довжина пароля повинна складати 6 символів!");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    if(user.isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, AddDB.class));
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this,"Перевірте свою електронну пошту, щоб підтвердити свій аккаунт",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Не вдалося увійти! Будь ласка, перевірте себе", Toast.LENGTH_LONG).show();

                }
            }
        });
            }

    public void goRegist(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, 101);
    }

             public void doLogin(View view) {
                userLogin();
                /* if(editTextPassword !=null || editTextEmail !=null)
                 {
                     Intent intent = new Intent(this, AddDB.class);
                     startActivityForResult(intent, 101);

                 }*/

             }
         }
