package com.example.cyrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.Logic.BusinessInterfaces.businessStringResponse;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.activities.AddBusinessPostActivity;
import com.example.cyrate.activities.BusinessPostFeed;

import org.json.JSONException;

public class EditBusinessPostActivity extends AppCompatActivity {

    Bundle extras;

    TextView busName;
    ImageView busImage, backBtn;
    Button submitBtn;
    EditText postTxt_et, photoUrl_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business_post);

        extras = getIntent().getExtras();

        busName = findViewById(R.id.editPost_busName);
        busImage = findViewById(R.id.editPost_busPhoto);
        backBtn = findViewById(R.id.back_btn_editPost);
        submitBtn = findViewById(R.id.editPost_submit);
        postTxt_et = findViewById(R.id.editPost_postTxt);
        photoUrl_et = findViewById(R.id.editPost_photoUrl);

        new ImageLoaderTask(extras.getString("IMAGE"), busImage).execute();
        busName.setText(extras.getString("NAME"));
        postTxt_et.setText(extras.getString("POST_TEXT"));
        photoUrl_et.setText(extras.getString("POST_PHOTO"));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBusinessPostActivity.this, BusinessPostFeed.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For now, we'll require a photo with every post just for the aesthetics of the layout
                if (postTxt_et.getText().toString().isEmpty() || photoUrl_et.getText().toString().isEmpty()){
                    Toast.makeText(EditBusinessPostActivity.this, "Incomplete Fields", Toast.LENGTH_LONG).show();
                }
                else{
                    String postTxt = postTxt_et.getText().toString();
                    String photoUrl = photoUrl_et.getText().toString();
                    int busId = extras.getInt("ID");

                    BusinessServiceLogic businessServiceLogic = new BusinessServiceLogic();

                    int postId = extras.getInt("POST_ID");
                    try {
                        businessServiceLogic.editPost(postId, postTxt, photoUrl, new businessStringResponse() {
                            @Override
                            public void onSuccess(String s) {
                                Log.d("EDIT POST ON SUCCESS", s);
                                Toast.makeText(EditBusinessPostActivity.this, "Post Updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditBusinessPostActivity.this, BusinessPostFeed.class);
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
                                Toast.makeText(EditBusinessPostActivity.this, "ERROR IN EDIT POST", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}