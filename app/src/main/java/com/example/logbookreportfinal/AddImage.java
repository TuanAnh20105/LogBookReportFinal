package com.example.logbookreportfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AddImage extends AppCompatActivity {
    Button btnPreview,btnAdd;
    EditText txt;
    ImageButton imgHome;
    ImageView img;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        txt  = findViewById(R.id.txtUrl);
        btnPreview =findViewById(R.id.btnPreview);
        btnAdd = findViewById(R.id.btnAddToList);
        imgHome = findViewById(R.id.btnHome);
        img = findViewById(R.id.imagePreview);

        btnPreview.setOnClickListener( view ->{
            String url = txt.getText().toString();
            DisplayImage task = new DisplayImage(mProgressDialog, this,img);
            task.execute(url);
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        });
        btnAdd.setOnClickListener(view ->{
            DatabaseHelper db = new DatabaseHelper(this);
            db.insertImage(txt.getText().toString());
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        });


        imgHome.setOnClickListener(view ->{
            Intent i = new Intent(AddImage.this,MainActivity.class);
            startActivity(i);
        });
    }

}