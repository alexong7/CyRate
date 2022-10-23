package com.example.cyrate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.Logic.ReviewInterfaces.reviewStringResponse;
import com.example.cyrate.Logic.ReviewServiceLogic;
import com.example.cyrate.R;

import org.json.JSONException;

public class AddReviewActivity extends AppCompatActivity {

    ImageView backBtn, submitBtn, busPhoto;
    TextView busName;
    RatingBar ratingBar;
    EditText reviewBody;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        backBtn = findViewById(R.id.back_btn_addReview);
        submitBtn = findViewById(R.id.submitReview_icon);
        busPhoto = findViewById(R.id.busImage_addReview);
        busName = findViewById(R.id.busName_addReview);
        ratingBar = findViewById(R.id.ratingBar_addReview);
        reviewBody = findViewById(R.id.reviewBody_addReview);

        extras = getIntent().getExtras();

        new ImageLoaderTask(extras.getString("IMAGE"), busPhoto).execute();
        busName.setText(extras.getString("NAME"));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddReviewActivity.this, ReviewListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reviewBody.getText().toString().isEmpty()){
                    Toast.makeText(AddReviewActivity.this, "Fill out the Review Body", Toast.LENGTH_LONG).show();
                }
                else{
                    String reviewTextBody = reviewBody.getText().toString();
                    int ratingVal = (int) ratingBar.getRating();
                    int busId = extras.getInt("ID");

                    ReviewServiceLogic reviewServiceLogic = new ReviewServiceLogic();

                    try {
                        reviewServiceLogic.addReview(busId, MainActivity.globalUser.getUserId(), reviewTextBody, ratingVal,
                                new reviewStringResponse() {
                                    @Override
                                    public void onSuccess(String s) {
                                        Toast.makeText(AddReviewActivity.this, "Review Added!", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(String s) {
                                        Toast.makeText(AddReviewActivity.this, "ERROR IN ADD REVIEW", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });



    }
}