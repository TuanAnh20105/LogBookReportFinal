package com.example.logbookreportfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Image> imageList;
    ImageButton btnPre,btnNext,btnTakePhoto,btnAddImage;
    int index = 0;
    ImageView img;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.imageViewOfCamera);
        DatabaseHelper db = new DatabaseHelper(this);
        imageList = db.getImages();
        loadImage();

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view ->{
            index++;
            if(index >imageList.size()-1)
            {
                index = 0;
            }
            loadImage();
        });
        btnPre = findViewById(R.id.btnPre);
        btnPre.setOnClickListener(view ->{
            index--;
            if(index < 0)
            {
                index = imageList.size()-1;
            }
            loadImage();
        });
        btnAddImage = findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this,AddImage.class);
            startActivity(i);
        });
        btnTakePhoto =findViewById(R.id.btnTakeImage);
        btnTakePhoto.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this,CameraX.class);
            startActivity(i);
        });

    }
    public void loadImage()
    {
        DisplayImage task = new DisplayImage(mProgressDialog, this,img);
        task.execute(imageList.get(index).getUrl().toString());
    }

}