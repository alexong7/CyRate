package com.example.cyrate.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.Logic.BusinessInterfaces.businessStringResponse;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.R;
import com.example.cyrate.net_utils.Utils;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddBusinessPostActivity extends AppCompatActivity {

    Bundle extras;

    TextView busName;
    ImageView busImage, backBtn, photoVerify;
    Button submitBtn, addPhoto;
    EditText postTxt_et;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_post);

        extras = getIntent().getExtras();

        busName = findViewById(R.id.addPost_busName);
        busImage = findViewById(R.id.addPost_busPhoto);
        backBtn = findViewById(R.id.back_btn_addPost);
        submitBtn = findViewById(R.id.addPost_submit);
        postTxt_et = findViewById(R.id.addPost_postTxt);
        addPhoto = findViewById(R.id.addPost_photoUrl);
        photoVerify = findViewById(R.id.addPost_photoCheck);

        new ImageLoaderTask(extras.getString("IMAGE"), busImage).execute();
        busName.setText(extras.getString("NAME"));

        // Green check to determine if a User as added a Photo for UX purposes
        photoVerify.setVisibility(View.INVISIBLE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBusinessPostActivity.this, BusinessPostFeed.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For now, we'll require a photo with every post just for the aesthetics of the layout
                if (postTxt_et.getText().toString().isEmpty()){
                    Toast.makeText(AddBusinessPostActivity.this, "Incomplete Fields", Toast.LENGTH_LONG).show();
                }
                if (bitmap == null){
                    Toast.makeText(AddBusinessPostActivity.this, "Add a photo!", Toast.LENGTH_LONG).show();
                }
                else{
                    String postTxt = postTxt_et.getText().toString();
                    String blobPhoto = Utils.imageToString(bitmap);
                    int busId = extras.getInt("ID");

                    BusinessServiceLogic businessServiceLogic = new BusinessServiceLogic();

                    try {
                        businessServiceLogic.addPost(busId, postTxt, blobPhoto, new businessStringResponse() {
                            @Override
                            public void onSuccess(String s) {
                                Log.d("ADD POST ON SUCCESS", s);
                                Toast.makeText(AddBusinessPostActivity.this, "Post Submitted!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddBusinessPostActivity.this, BusinessPostFeed.class);
                                intent.putExtras(extras);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(intent);
                                    }
                                }, 2000);
                            }

                            @Override
                            public void onError(String s) {
                                Toast.makeText(AddBusinessPostActivity.this, "ERROR IN ADD POST", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Utils.IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utils.IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                if(bitmap != null){
                    photoVerify.setVisibility(View.VISIBLE);
                }
                else{
                    photoVerify.setVisibility(View.INVISIBLE);
                }
                Log.d("Bitmap", bitmap.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

