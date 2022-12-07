package com.example.cyrate.activities;

import static com.example.cyrate.activities.MainActivity.globalUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.Logic.FavoritesServiceLogic;
import com.example.cyrate.Logic.ReviewInterfaces.getReviewsResponse;
import com.example.cyrate.Logic.ReviewServiceLogic;
import com.example.cyrate.NavMenuUtils;
import com.example.cyrate.R;
import com.example.cyrate.adapters.BusinessListAdapter;
import com.example.cyrate.adapters.ReviewListAdapter;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.BusinessRecyclerViewInterface;
import com.example.cyrate.models.ReviewRecyclerViewInterface;
import com.example.cyrate.models.ReviewListCardModel;
import com.google.android.material.navigation.NavigationView;
import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class WelcomeToCyRateActivity extends AppCompatActivity implements ReviewRecyclerViewInterface, BusinessRecyclerViewInterface, NavigationView.OnNavigationItemSelectedListener {
    TextView name, username, email, phone;
    Button editProfileButton;
    ImageView profilePic;

    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView open_menu;

    FavoritesServiceLogic favoritesServiceLogic;

    RecyclerView favoritesRecycler;

    BusinessListAdapter busListAdapter;
    LinearLayoutManager favoritesLayoutManager;
    LinearLayoutManager reviewsLayoutManager;
    ArrayList<BusinessListCardModel> businessListCardModel = new ArrayList<>();

    ReviewServiceLogic reviewServiceLogic;
    RecyclerView reviewsRecycler;
    ReviewListAdapter reviewListAdapter;
    ArrayList<ReviewListCardModel> reviewListCardModels = new ArrayList<>();

    ImageView favoritesButton, myReviewsButton;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_to_cyrate);

        Log.d("welcome", "looking for profile elements");

        // Profile elements
        name = findViewById(R.id.name_text);
        Log.d("welcome", "found name");
        username = findViewById(R.id.username_text);
        Log.d("welcome", "found username");
        email = findViewById(R.id.email_text);
        Log.d("welcome", "found email");
        phone = findViewById(R.id.phone_text);
        Log.d("welcome", "found phone");
        editProfileButton = findViewById(R.id.btn_edit_profile);
        Log.d("welcome", "found button");
        profilePic = findViewById(R.id.profilePicPic);
        Log.d("welcome", "found profile pic");

        Log.d("welcome", "setting profile elements");

        name.setText(globalUser.getFullName());
        username.setText(globalUser.getUsername());
        email.setText(globalUser.getEmail());
        new ImageLoaderTask(globalUser.getPhotoUrl(), profilePic).execute();

        //menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        open_menu = findViewById(R.id.open_menu_icon);

        navigationDrawer();

        drawerLayout.setScrimColor(getResources().getColor(R.color.red));

        // Use this to hide any menu tabs depending on the user type
        NavMenuUtils.hideMenuItems(navView.getMenu());


        //favorites
        favoritesRecycler = findViewById(R.id.favorites_recycler);
        favoritesLayoutManager = new LinearLayoutManager(this);

        favoritesServiceLogic = new FavoritesServiceLogic();
        busListAdapter = new BusinessListAdapter(this,
                businessListCardModel, this);

        favoritesRecycler.setAdapter(busListAdapter);
        favoritesRecycler.setLayoutManager(favoritesLayoutManager);

        try {
            setFavorites();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //reviews
        reviewsRecycler = findViewById(R.id.reviews_recycler);
        reviewsLayoutManager = new LinearLayoutManager(this);
        reviewServiceLogic = new ReviewServiceLogic();
        reviewListAdapter = new ReviewListAdapter(this, reviewListCardModels, this);

        reviewsRecycler.setAdapter(reviewListAdapter);
        reviewsRecycler.setLayoutManager(reviewsLayoutManager);

        try{
            setReviews();
        } catch (JSONException e){
            e.printStackTrace();
        }

        Log.d("welcome", "setting lsitener");
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeToCyRateActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });

        favoritesButton = findViewById(R.id.favorites_button);
        myReviewsButton = findViewById(R.id.reviews_button);

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FUTURE: navigate to favorites list
                Intent i = new Intent(WelcomeToCyRateActivity.this, FavoritesActivity.class);
                startActivity(i);
            }
        });

        myReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeToCyRateActivity.this, PersonalReviewListActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * gets the favorites for the current user
     * @throws JSONException
     */
    private void setFavorites() throws JSONException {
        favoritesServiceLogic.getFavoritesByUser(MainActivity.globalUser.getUserId(), new getBusinessesResponse() {
            @Override
            public void onSuccess(List<BusinessListCardModel> list) {
                for (int i = 0; i < list.size(); i++) {
                    businessListCardModel.add(list.get(i));
                }
                Log.d("TEST 1", "IN HERE");
                busListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s) {
                Log.d("set favorites", s);
                Toast.makeText(WelcomeToCyRateActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * gets the reviews written by the current user
     * @throws JSONException
     */
    private void setReviews() throws JSONException{
        ///merge main to get new review service logic methods
        reviewServiceLogic.getReviewsByUser(globalUser.getUserId(), new getReviewsResponse() {
            @Override
            public void onSuccess(List<ReviewListCardModel> list) {
                for (int i = 0; i < list.size(); i++){
                    reviewListCardModels.add(list.get(i));
                }
                reviewListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s) {
                Log.d("set reviews", s);
                Toast.makeText(WelcomeToCyRateActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigationDrawer() {
        // Navigation Drawer
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);

        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    /**
     * navigates to desired page and closes nav drawer
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.nav_home){
            NavMenuUtils.onNavItemSelected(menuItem, WelcomeToCyRateActivity.this);
        }
        else{
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * navigate to business page
     * @param position
     */
    @Override
    public void onBusinessClick(int position) {
        Intent intent = new Intent(this, IndividualBusinessActivity.class);

        intent.putExtra("NAME", businessListCardModel.get(position).getBusName());
        intent.putExtra("CATEGORY", businessListCardModel.get(position).getBusType());
        intent.putExtra("ADDRESS", businessListCardModel.get(position).getLocation());
        intent.putExtra("HOURS", businessListCardModel.get(position).getHours());
        intent.putExtra("IMAGE", businessListCardModel.get(position).getPhotoUrl());
        intent.putExtra("PRICE_GAUGE", businessListCardModel.get(position).getPriceGauge());
        intent.putExtra("ID", businessListCardModel.get(position).getBusId());
        intent.putExtra("RATING_SUM", businessListCardModel.get(position).getReviewSum());
        intent.putExtra("REVIEW_COUNT", businessListCardModel.get(position).getReviewCount());
        intent.putExtra("PREVIOUS_ACTIVITY", "WelcomeToCyrateActivity");

        startActivity(intent);
    }

    /**
     * navigates to individual review page
     * @param position
     */
    @Override
    public void onReviewClick(int position){
        Intent intent = new Intent(this, IndividualReviewActivity.class);
        // Put in new extras for review info + prev extras (business info)
//        intent.putExtras(extras);
        intent.putExtra("PREVIOUS_ACTIVITY", "WelcomeToCyrateActivity");
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
