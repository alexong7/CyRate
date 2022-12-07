package com.example.cyrate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyrate.Logic.BusinessInterfaces.getBusinessPostsByID;
import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.R;
import com.example.cyrate.UserType;
import com.example.cyrate.adapters.BusinessFeedAdapter;
import com.example.cyrate.adapters.BusinessListAdapter;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.BusinessPostCardModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BusinessPostFeed extends AppCompatActivity {

    BusinessServiceLogic businessServiceLogic;
    BusinessFeedAdapter busFeedAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<BusinessPostCardModel> businessPostList = new ArrayList<>();
    Bundle extras;

    ImageView back_btn, addPost_btn;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_post_feed);

        extras = getIntent().getExtras();
        back_btn = findViewById(R.id.busFeed_backBtn);
        addPost_btn = findViewById(R.id.busFeed_addPost);
        emptyView = findViewById(R.id.empty_view_busFeed);

        // Make this only visible for admins for now, since we don't have
        // business accounts set up entirely yet.
        if(!MainActivity.globalUser.getUserType().equals(UserType.ADMIN)){
            addPost_btn.setVisibility(View.INVISIBLE);
        }

        // Set this to non-visible initially
        emptyView.setVisibility(View.GONE);


        RecyclerView recyclerView = findViewById(R.id.busFeed_recyclerView);
        layoutManager = new LinearLayoutManager(this);

        businessServiceLogic = new BusinessServiceLogic();
        busFeedAdapter = new BusinessFeedAdapter(this,
                businessPostList, extras);

        Log.d("TEST 1", "BEFORE SET ADAPTER");
        recyclerView.setAdapter(busFeedAdapter);
        recyclerView.setLayoutManager(layoutManager);

        try {
            Log.d("TEST 1", "BEFORE SET BUS LIST CARD MODELS");
            setUpBusinessPostModels(recyclerView, emptyView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusinessPostFeed.this, IndividualBusinessActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        addPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusinessPostFeed.this, AddBusinessPostActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void setUpBusinessPostModels(RecyclerView recyclerView, TextView emptyView) throws JSONException {
        int busId = extras.getInt("ID");

        businessServiceLogic.getBusinessPostsByID(busId, new getBusinessPostsByID() {
            @Override
            public void onSuccess(List<BusinessPostCardModel> list) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    businessPostList.add(list.get(i));
                }
                Log.d("TEST 1", "IN HERE");
                busFeedAdapter.notifyDataSetChanged();

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
                Log.d("TEST 1", s);
                Toast.makeText(BusinessPostFeed.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }
}