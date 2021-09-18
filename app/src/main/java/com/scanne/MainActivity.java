package com.scanne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity /*implements ReadActivity.CustomListListener*/ {
    ImageView imageView;
    EditText editView;

    DatabaseReference firebaseDatabase;
    RecyclerView recyclerView;
public ArrayList<DataBase> arrayList;
     ArrayAdapter<String> adapter;
ReadActivity readActivity;

  /*  @Override
    public void onListChanged(List<DataBase> myList) {

    }*/
/*   private static final int CAMERA_REQUEST_CODE = 500;
    private static final int STORAGE_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_GALLERY_CODE = 1100;
    private static final int IMAGE_PICK_CAMERA_CODE = 1200;
    String[] cameraPermission;
    String[] storagePermission;
    Uri image_uri;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   bunt = (Button) findViewById(R.id.login);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageId);
        editView = findViewById(R.id.textId);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 181);
        }
    }


    ///START ONCLICKS
    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 101);
    }

    public void doScann(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    public void doGallery(View view) {
        Intent intent = new Intent(this, ReadActivity.class);
        startActivityForResult(intent, 101);
       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_GALLERY_CODE);*/

    }

    public void goAdmin(View view) {
        Intent intent = new Intent(this, AddDB.class);
        startActivityForResult(intent, 101);
    }
    //END ONCLICKS

    public void searchResult() {
        if (editView != null) {
            //   Query query = FirebaseDatabase.getInstance().getReference("Eadd").orderByChild("name").equalTo(editView);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
