package com.scanne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {
    private TextView tvnameE, tvEcode, tvcateg,tvlevelD, tvorigin, tvedes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        init();
        getIntentMain();
    }
    private void init()
    {

        tvnameE= findViewById(R.id.e_name);
        tvEcode=findViewById(R.id.e_num);
        tvcateg=findViewById(R.id.e_c);
        tvlevelD=findViewById(R.id.e_level);
        tvorigin=findViewById(R.id.e_origin);
        tvedes=findViewById(R.id.e_description);
    }
    private void getIntentMain()
    {
        Intent i =getIntent();
        if(i!=null)
        {
            tvnameE.setText(i.getStringExtra(Constant.E_NAME));
            tvEcode.setText(i.getStringExtra(Constant.E_NUMBER));
            tvcateg.setText(i.getStringExtra(Constant.E_CATEGORY));
            tvlevelD.setText(i.getStringExtra(Constant.E_LEVEL));
            tvorigin.setText(i.getStringExtra(Constant.E_ORIGIN));
            tvedes.setText(i.getStringExtra(Constant.E_DESCRIPTION));
        }
    }
}
