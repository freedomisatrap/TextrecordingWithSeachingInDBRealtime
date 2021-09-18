package com.scanne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ReadActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editView;
 //  private AutoCompleteTextView txtSearch;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<DataBase> listTemp ;

    private static final int IMAGE_PICK_GALLERY_CODE = 1100;


    public ReadActivity() {
    }
    private DatabaseReference mDatabaseReference;
    private String E_KEY = "Eadd";
   // FirebaseListOptions<DataBase> options;
    //FirebaseListAdapter<DataBase> adapterr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        imageView = findViewById(R.id.imageId2);
        editView = (EditText) findViewById(R.id.textId1);
        listView = findViewById(R.id.listE);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 181);
        }
        init();
        getDataFromDB();
        setOnClickItem();
        editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ReadActivity.this.adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0,
                                          int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private void init() {
      //  listView = findViewById(R.id.listE);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(E_KEY);
    }

    private void getDataFromDB()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(listData.size()>0)listData.clear();
                if(listTemp.size()>0)listTemp.clear();
for(DataSnapshot ds: snapshot.getChildren())
{
    DataBase dataBase = ds.getValue(DataBase.class);
    assert dataBase !=null;
    listData.add(dataBase.eadd);
    listTemp.add(dataBase);
}
adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addValueEventListener(vListener);
    }
    private void setOnClickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataBase dataBase = listTemp.get(position);
                Intent i = new Intent(ReadActivity.this,ShowActivity.class);
                i.putExtra(Constant.E_NUMBER,dataBase.eadd);
                i.putExtra(Constant.E_NAME,dataBase.name);
                i.putExtra(Constant.E_CATEGORY,dataBase.category);
                i.putExtra(Constant.E_ORIGIN,dataBase.origin);
                i.putExtra(Constant.E_LEVEL,dataBase.leveldanger);
                i.putExtra(Constant.E_DESCRIPTION,dataBase.description);
                startActivity(i);

            }
        });
    }
// clicks
    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 101);
    }

    public void doScann(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    public void doGallery(View view) {
      /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        Intent intent = new Intent(Intent.ACTION_PICK);
      intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_GALLERY_CODE);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
        //   return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.admin)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 101);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  assert data != null;
        Bundle bundle = data.getExtras();
        //from bundle, extract the image
        Bitmap bitmap = (Bitmap) bundle.get("data");
        //set image in imageview
        imageView.setImageBitmap(bitmap);
        //process the image
        //1. create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        //2. Get an instance of FirebaseVision
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        //3. Create an instance of FirebaseVisionTextRecognizer
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        //4. Create a task to process the image
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        //5. if task is success
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String s = firebaseVisionText.getText();
                editView.setText(s);
            /*    mDatabaseReference.orderByChild("eadd")
                        .startAt(s)
                        .endAt(s+"\uf8ff");*/
                // Query query = FirebaseDatabase.getInstance().getReference("Eadd").orderByChild("name").equalTo(s);

            }
        });
        //6. if task is failure
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }





    }
