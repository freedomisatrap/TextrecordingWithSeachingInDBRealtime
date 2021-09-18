package com.scanne;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class  AddDB extends AppCompatActivity {
    private EditText input_nameE, input_Ecode, input_categ, input_levelD, input_origin, input_edes;
    private Button buttonSave, buttonRead;

    // private ListView list_data;
    private ProgressBar circular_progress;

    private FirebaseDatabase mFirebaseDatabase;

    // private List<DataBase> list = new ArrayList<>();
    private DataBase selectedDB;
    private DatabaseReference mDatabaseReference;
    private String E_KEY="Eadd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_db);

        buttonSave = (Button) findViewById(R.id.buttonsave);
        buttonRead = (Button) findViewById(R.id.buttondelete);
        //initFirebase();
        init();

        // addEventFirebaseListener();
        // buttonDelete =  (Button) findViewById(R.id.deleteE);
        // buttonBack= (Button)findViewById(R.id.back);
        // buttonAdd=(Button)findViewById(R.id.addE);
    } public void init()
    {
        input_nameE= findViewById(R.id.edname);
        input_Ecode=findViewById(R.id.ednum);
        input_categ=findViewById(R.id.edcat);
        input_levelD=findViewById(R.id.edlev);
        input_origin=findViewById(R.id.edor);
        input_edes=findViewById(R.id.eddis);

        mDatabaseReference =FirebaseDatabase.getInstance().getReference(E_KEY);
    }

    public void onClickSave(View view)
    {
        String id=mDatabaseReference.getKey();
        String nameE=input_nameE.getText().toString();
        String numberE=input_Ecode.getText().toString();
        String levelE=input_levelD.getText().toString();
        String originE=input_origin.getText().toString();
        String categoryE= input_categ.getText().toString();
        String descriptionE= input_edes.getText().toString();
        DataBase dataBase = new DataBase(id,numberE,nameE,categoryE,descriptionE,originE);
        mDatabaseReference.push().setValue(dataBase);
        if(!TextUtils.isEmpty(nameE)&& !TextUtils.isEmpty(numberE)&& !TextUtils.isEmpty(levelE)&& !TextUtils.isEmpty(originE)&& !TextUtils.isEmpty(categoryE)&& !TextUtils.isEmpty(descriptionE))
        {
            mDatabaseReference.push().setValue(dataBase);
            Toast.makeText(this,"Save happened",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
        }
    }
    public void goOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, ReadActivity.class));
    }

    public void onClickDelete(View view)
    {
        deleteDB(selectedDB);
    }

    public void onClickUp(View view) {
        DataBase eadd = new DataBase(input_nameE.getText().toString(), input_Ecode.getText().toString(), input_categ.getText().toString(), input_levelD.getText().toString(), input_origin.getText().toString(), input_edes.getText().toString());
        updateDB(eadd);
    }
    private void clearEditText() {
        input_nameE.setText("");
        input_Ecode.setText("");
        input_categ.setText("");
        input_levelD.setText("");
        input_origin.setText("");
        input_edes.setText("");
    }

    private void updateDB( DataBase dataBase) {

        mDatabaseReference.child("Eadd")
                .child(dataBase.getEadd())
                .child("Ecode")
                .setValue(dataBase.getEadd());
        mDatabaseReference.child("Eadd")
                .child(dataBase.getName())
                .child("Name")
                .setValue(dataBase.getName());
        mDatabaseReference.child("Eadd")
                .child(dataBase.getCategory())
                .child("Category")
                .setValue(dataBase.getCategory());
        mDatabaseReference.child("Eadd")
                .child(dataBase.getLeveldanger())
                .child("Level")
                .setValue(dataBase.getLeveldanger());
        mDatabaseReference.child("Eadd")
                .child(dataBase.getOrigin())
                .child("Origin")
                .setValue(dataBase.getOrigin());
        mDatabaseReference.child("Eadd")
                .child(dataBase.getDescription())
                .child("Description")
                .setValue(dataBase.getDescription());
        clearEditText();
    }
    private void deleteDB(DataBase selectedDB) {
        mDatabaseReference.child("Eadd")
                .child(selectedDB.getEadd())
                .removeValue();
        clearEditText();
    }
}