package com.example.cyrate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.EditReviewActivity;
import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.Logic.ReviewInterfaces.reviewStringResponse;
import com.example.cyrate.Logic.ReviewServiceLogic;
import com.example.cyrate.R;
import com.example.cyrate.UserType;
import com.example.cyrate.net_utils.Const;

import org.json.JSONException;

public class IndividualReviewActivity extends AppCompatActivity {

    ImageView back_btn, reviewerProfilePic, deleteIcon, thumbsUpIcon, commentIcon, editReviewIcon;
    TextView reviewerName, reviewBody, reviewHeading;
    RatingBar ratingBar;
    Bundle extras;
    String previousActivity;

    ReviewServiceLogic reviewServiceLogic;
    int reviewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_review);

        back_btn = (ImageView) findViewById(R.id.back_button_image);
        editReviewIcon = findViewById(R.id.editReview_icon);
        reviewerProfilePic = findViewById(R.id.profile_pic);
        deleteIcon = findViewById(R.id.deleteReviewIcon);
        thumbsUpIcon = findViewById(R.id.thumbsUpIcon);
        commentIcon = findViewById(R.id.commentIcon);

        reviewerName = findViewById(R.id.reviewerNameIndiv);
        reviewHeading = findViewById(R.id.reviewHeading_individualReview);
        reviewBody = findViewById(R.id.reviewBody);
        ratingBar = findViewById(R.id.reviewRating);

        extras = getIntent().getExtras();

        reviewServiceLogic = new ReviewServiceLogic();

        new ImageLoaderTask(extras.getString("REVIEWER_PROFILE_PIC"), reviewerProfilePic).execute();
        reviewerName.setText(extras.getString("REVIEWER_USERNAME"));
        reviewHeading.setText(extras.getString("REVIEW_HEADING"));
        reviewBody.setText(extras.getString("REVIEW_BODY"));
        ratingBar.setRating(extras.getInt("RATING_VAL"));
        reviewId = extras.getInt("REVIEW_ID");



        // Remove the delete & edit icons if the current User is not the original
        // reviewer or not an Admin
        deleteIcon.setVisibility(View.GONE);
        editReviewIcon.setVisibility(View.INVISIBLE);

        // Update the thumbsUpIcon and CommentIcon position since we removed the deleteIcon
        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintSet_individualReview);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);

        cs.setHorizontalBias(R.id.thumbsUpIcon, (float) 0.4);
        cs.setHorizontalBias(R.id.commentIcon, (float) 0.6);
        cs.applyTo(cl);

        int originalReviewerId = extras.getInt("REVIEWER_ID");
        if (MainActivity.globalUser.getUserId() == originalReviewerId ||
                MainActivity.globalUser.getUserType() == UserType.ADMIN) {

            cs.setHorizontalBias(R.id.thumbsUpIcon, (float) 0.3);
            cs.setHorizontalBias(R.id.commentIcon, (float) 0.5);
            cs.applyTo(cl);

            deleteIcon.setVisibility(View.VISIBLE);
            editReviewIcon.setVisibility(View.VISIBLE);
        }

        editReviewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualReviewActivity.this, EditReviewActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualReviewActivity.this, CommentThreadActivity.class);
                intent.putExtras(extras);
                intent.putExtra(Const.COMMENT_TYPE, Const.REVIEW_COMMENT);
                intent.putExtra(Const.ID_FOR_COMMENT, reviewId);
                startActivity(intent);
            }
        });



        previousActivity = extras.getString("PREVIOUS_ACTIVITY");

        // Navigates back to review list
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (previousActivity.equals("PersonalReviewListActivity")){

                    intent = new Intent(IndividualReviewActivity.this, PersonalReviewListActivity.class);
                }
                else if(previousActivity.equals("WelcomeToCyrateActivity")){
                    intent = new Intent(IndividualReviewActivity.this, WelcomeToCyRateActivity.class);
                }
                else{

                    intent = new Intent(IndividualReviewActivity.this, ReviewListActivity.class);
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IndividualReviewActivity.this);

                builder.setMessage("Are you sure you want to delete this review?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    reviewServiceLogic.deleteReview(reviewId,
                                            new reviewStringResponse() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    Toast.makeText(IndividualReviewActivity.this,
                                                            "Successfully Deleted Review!", Toast.LENGTH_LONG).show();

                                                    final Handler handler = new Handler();


                                                    Intent intent = new Intent(IndividualReviewActivity.this, ReviewListActivity.class);
                                                    intent.putExtras(extras);

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startActivity(intent);
                                                        }
                                                    }, 3000);
                                                }

                                                @Override
                                                public void onError(String s) {
                                                    Log.d("DELETE REVIEW ERROR", s);
                                                    Toast.makeText(IndividualReviewActivity.this, s, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}