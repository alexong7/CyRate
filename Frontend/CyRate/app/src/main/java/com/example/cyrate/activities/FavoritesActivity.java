package com.example.cyrate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.Logic.FavoritesServiceLogic;
import com.example.cyrate.R;
import com.example.cyrate.adapters.BusinessListAdapter;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.BusinessRecyclerViewInterface;
import com.example.cyrate.models.ReviewRecyclerViewInterface;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements BusinessRecyclerViewInterface, NavigationView.OnNavigationItemSelectedListener {

    FavoritesServiceLogic favoritesServiceLogic;
    BusinessListAdapter busListAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<BusinessListCardModel> businessListCardModel = new ArrayList<>();

    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView open_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        open_menu = (ImageView) findViewById(R.id.open_menu_icon);

        // Use this to hide any menu tabs depending on the user type
        hideMenuItems();

        RecyclerView recyclerView = findViewById(R.id.favorites_recyclerView);
        layoutManager = new LinearLayoutManager(this);

        favoritesServiceLogic = new FavoritesServiceLogic();
        busListAdapter = new BusinessListAdapter(this,
                businessListCardModel, this);

        Log.d("TEST 1", "BEFORE SET ADAPTER");
        recyclerView.setAdapter(busListAdapter);
        recyclerView.setLayoutManager(layoutManager);


        try {
            Log.d("TEST 1", "BEFORE SET BUS LIST CARD MODELS");
            setUpBusinessListCardModels();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        navigationDrawer();

        drawerLayout.setScrimColor(getResources().getColor(R.color.red));

    }


    // When menu is open and back button is pressed, we just close the menu instead of going
    // back a page
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void setUpBusinessListCardModels() throws JSONException {
        favoritesServiceLogic.getFavoritesByUser(MainActivity.globalUser.getUserId(), new getBusinessesResponse() {
            @Override
            public void onSuccess(List<BusinessListCardModel> list) {
                // createa a new List<BusinessCardModel>
                //
                // for every favoriteCard in list
                // add favoriteCard.business to businessCard List
                Log.d("favs list", "in success");
                for (int i = 0; i < list.size(); i++) {
                    businessListCardModel.add(list.get(i));
                    Log.d("favs list",list.get(i).toString());
                }
                Log.d("TEST 1", "IN HERE");
                busListAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(String s) {
                Log.d("TEST 1", s);
                Toast.makeText(FavoritesActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    // onClick for each card in the list
    public void onBusinessClick(int position) {
        Intent intent = new Intent(this, IndividualBusinessActivity.class);

        intent.putExtra("NAME", businessListCardModel.get(position).getBusName());
        intent.putExtra("CATEGORY", businessListCardModel.get(position).getBusType());
        intent.putExtra("ADDRESS", businessListCardModel.get(position).getLocation());
        intent.putExtra("RATING", "4.7"); // Hard coded for now
        intent.putExtra("HOURS", businessListCardModel.get(position).getHours());
        intent.putExtra("IMAGE", businessListCardModel.get(position).getPhotoUrl());
        intent.putExtra("PRICE_GAUGE", businessListCardModel.get(position).getPriceGauge());
        intent.putExtra("ID", businessListCardModel.get(position).getBusId());
        intent.putExtra("PREVIOUS_ACTIVITY", "FavoritesActivity");


        startActivity(intent);
    }

    private void navigationDrawer() {
        // Navigation Drawer
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_restaurants);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent i;

        switch(menuItem.getItemId()){
            case R.id.nav_restaurants:
                i = new Intent(FavoritesActivity.this, BusinessListActivity.class);
                i.putExtra("PREVIOUS_ACTIVITY", "FavoritesActivity");

                startActivity(i);
                break;
            case R.id.nav_addBusiness:
                i = new Intent(FavoritesActivity.this, AddBusinessActivity.class);
                i.putExtra("PREVIOUS_ACTIVITY", "FavoritesActivity");
                startActivity(i);
                break;
            case R.id.nav_edit_profile:
                i = new Intent(FavoritesActivity.this, EditProfileActivity.class);
                i.putExtra("PREVIOUS_ACTIVITY", "FavoritesActivity");
                startActivity(i);
                break;
            case R.id.nav_logout:
                i = new Intent(FavoritesActivity.this, LoginActivity.class);
                startActivity(i);
            case R.id.nav_home:
                i = new Intent(FavoritesActivity.this, WelcomeToCyRateActivity.class);
                startActivity(i);

            case R.id.nav_favorites:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideMenuItems(){
        Menu navMenu = navView.getMenu();

        if (MainActivity.globalUser.getEmail().equals("guest-user-email")){
            // A guest user should not be able to edit the guest user profile
            navMenu.findItem(R.id.nav_edit_profile).setVisible(false);

            //guest cannot add business
            navMenu.findItem(R.id.nav_addBusiness).setVisible(false);

            //guest cannot log out
            navMenu.findItem(R.id.nav_logout).setVisible(false);

            //guest CAN sign in
            navMenu.findItem(R.id.nav_sign_in).setVisible(false);

        }
    }

}
