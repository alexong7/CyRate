package com.example.cyrate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.Logic.BusinessInterfaces.getBusinessByIDResponse;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.Logic.ReviewInterfaces.getReviewsResponse;
import com.example.cyrate.Logic.ReviewServiceLogic;
import com.example.cyrate.R;
import com.example.cyrate.UserType;
import com.example.cyrate.adapters.ReviewListAdapter;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.ReviewRecyclerViewInterface;
import com.example.cyrate.models.ReviewListCardModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity implements ReviewRecyclerViewInterface {

    ReviewServiceLogic reviewServiceLogic;
    BusinessServiceLogic businessServiceLogic;

    ReviewListAdapter reviewListAdapter;
    ArrayList<ReviewListCardModel> reviewListCardModels = new ArrayList<>();

    ImageView back_btn, addReview_btn;
    RatingBar ratingBar;
    TextView ratingTxt, reviewCount, noReviewTxt;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        extras = getIntent().getExtras();

        back_btn = (ImageView) findViewById(R.id.back_btn_icon);
        addReview_btn = findViewById(R.id.addReviewIcon);
        ratingBar = findViewById(R.id.ratingBar);
        ratingTxt = findViewById(R.id.ratingValue);
        reviewCount = findViewById(R.id.reviewsCount);
        noReviewTxt = findViewById(R.id.noReviewsText);

        // Guest users should not be able to add a review
        if (MainActivity.globalUser.getUserType() == UserType.GUEST) {
            addReview_btn.setVisibility(View.GONE);
        }

        if (MainActivity.globalUser.getUserType().equals(UserType.BUSINESS_OWNER)){
            addReview_btn.setVisibility(View.GONE);
        }


        RecyclerView recyclerView = findViewById(R.id.reviewList_recyclerView);
        TextView emptyView = findViewById(R.id.empty_view);

        // Set this to non-visible initially
        emptyView.setVisibility(View.GONE);

        businessServiceLogic = new BusinessServiceLogic();

        reviewServiceLogic = new ReviewServiceLogic();
        reviewListAdapter = new ReviewListAdapter(
                this, reviewListCardModels, this

        );
        recyclerView.setAdapter(reviewListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            setUpReviewModels(recyclerView, emptyView);
            setRatingAndReviewCount();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewListActivity.this, IndividualBusinessActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        addReview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewListActivity.this, AddReviewActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void setUpReviewModels(RecyclerView recyclerView, TextView emptyView) throws JSONException {
        int busId = extras.getInt("ID");
        reviewServiceLogic.getReviews(busId, new getReviewsResponse() {
            @Override
            public void onSuccess(List<ReviewListCardModel> list) {
                for (int i = 0; i < list.size(); i++) {
                    reviewListCardModels.add(list.get(i));
                }
                Log.d("TEST 2", "IN HERE");
                reviewListAdapter.notifyDataSetChanged();

                if (list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String s) {
                Log.d("TEST 2", s);
                Toast.makeText(ReviewListActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRatingAndReviewCount() throws JSONException {
        int busId = extras.getInt("ID");
        businessServiceLogic.getBusinessesById(busId, new getBusinessByIDResponse() {
            @Override
            public void onSuccess(BusinessListCardModel business) {
                int totalReviews = business.getReviewCount();
                int ratingSum = business.getReviewSum();

                float avgRating = totalReviews == 0 ? 0 : (float) ratingSum / totalReviews;
                ratingTxt.setText(String.format("%.1f",avgRating));
                reviewCount.setText(String.valueOf(totalReviews));
                extras.putInt("RATING_SUM", ratingSum);
                extras.putInt("REVIEW_COUNT", totalReviews);

                if (totalReviews == 0) {
                    ratingTxt.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    noReviewTxt.setVisibility(View.VISIBLE);
                } else {
                    ratingTxt.setVisibility(View.VISIBLE);
                    ratingBar.setVisibility(View.VISIBLE);
                    noReviewTxt.setVisibility(View.GONE);
                    ratingBar.setRating(avgRating);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onError(String s) {
                Log.d("setRatingAndReviewCount ERROR", s);
            }
        });

    }

    @Override
    // onClick for each card in the list
    public void onReviewClick(int position) {
        Intent intent = new Intent(ReviewListActivity.this, IndividualReviewActivity.class);
        // Put in new extras for review info + prev extras (business info)
        intent.putExtras(extras);
        intent.putExtra("PREVIOUS_ACTIVITY", "ReviewListActivity");
        intent.putExtra("REVIEWER_NAME", reviewListCardModels.get(position).getReviewUser().getFullName());
        intent.putExtra("RATING_VAL", reviewListCardModels.get(position).getRateVal());
        intent.putExtra("REVIEW_BODY", reviewListCardModels.get(position).getReviewText());
        intent.putExtra("REVIEWER_PROFILE_PIC", reviewListCardModels.get(position).getReviewUser().getPhotoUrl());
        intent.putExtra("REVIEWER_USERNAME", reviewListCardModels.get(position).getReviewUser().getUsername());


        intent.putExtra("REVIEW_HEADING", reviewListCardModels.get(position).getReviewHeader());
        intent.putExtra("REVIEW_ID", reviewListCardModels.get(position).getReviewId());
        intent.putExtra("REVIEWER_ID", reviewListCardModels.get(position).getReviewUser().getUserId());

        startActivity(intent);
    }

}