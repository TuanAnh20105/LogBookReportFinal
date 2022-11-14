package com.example.logbookreportfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraX extends AppCompatActivity implements ImageAnalysis.Analyzer  {
    PreviewView previewView;
    ImageCapture imageCapture;
    VideoCapture videoCapture;
    Executor ex = Executors.newSingleThreadExecutor();
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA","android.permission.RECORD_AUDIO"};

    ImageView imageView;
    EditText inputPictureURi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_x);
        previewView = findViewById(R.id.previewViewCamera);
        imageView = findViewById(R.id.imageViewCamera2);
        inputPictureURi = findViewById(R.id.inputUriPicture);
        Button btnTakeAPhoto = findViewById(R.id.btnAction);
        cameraStart();

        btnTakeAPhoto.setOnClickListener(view ->{
            takeAPhoto();
            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
        });
    }
    private void takeAPhoto() {
        long timeMillis = System.currentTimeMillis();
        ImageCapture.OutputFileOptions option1 = new ImageCapture.OutputFileOptions.Builder(new File(getApplicationContext().getFilesDir(),
                String.valueOf(timeMillis))).build();
        imageCapture.takePicture(
                option1,
                ex,
                new ImageCapture.OnImageSavedCallback(){
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults){
                        runOnUiThread(()->{
                            final Uri selectedImage = outputFileResults.getSavedUri();
                            try {
                                inputPictureURi.setText(outputFileResults.getSavedUri().getPath());
                                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage));
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        });
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                    }
                }
        );
    }
    private void cameraStart()
    {
        final ListenableFuture<ProcessCameraProvider> camera = ProcessCameraProvider.getInstance(this);
        camera.addListener(()-> {
            try {
                ProcessCameraProvider cameraProvider =  camera.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },ContextCompat.getMainExecutor(this));
    }
    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview pv = new Preview.Builder().build();
        pv.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
        videoCapture= new VideoCapture.Builder().setVideoFrameRate(30).build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ex,this);
        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,pv,imageCapture,videoCapture);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permission, grantResults);
        switch (requestCode)
        {
            case REQUEST_CODE_PERMISSIONS:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                }
        }
    }



    @Override
    public void analyze(@NonNull ImageProxy image)
    {
        image.close();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}