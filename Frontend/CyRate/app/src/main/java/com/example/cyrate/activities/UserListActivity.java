package com.example.cyrate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyrate.Logic.UserInterfaces.getAllUsersResponse;
import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.NavMenuUtils;
import com.example.cyrate.R;
import com.example.cyrate.adapters.UserListAdapter;
import com.example.cyrate.models.UserListCardModel;
import com.example.cyrate.models.UserRecyclerViewInterface;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserRecyclerViewInterface, NavigationView.OnNavigationItemSelectedListener {

    UserLogic userLogic;
    UserListAdapter userListAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<UserListCardModel> userListCardModel = new ArrayList<>();
    int[] profileImages = {R.drawable.profilepic};

    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView open_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        open_menu = findViewById(R.id.open_menu_icon);

        NavMenuUtils.hideMenuItems(navView.getMenu());

        RecyclerView recyclerView = findViewById(R.id.userList_recyclerView);
        layoutManager = new LinearLayoutManager(this);

        userLogic = new UserLogic();
        userListAdapter = new UserListAdapter(this, userListCardModel, this);

        recyclerView.setAdapter(userListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        navigationDrawer();

        drawerLayout.setScrimColor(getResources().getColor(R.color.red));
        drawerLayout.setScrimColor(getResources().getColor(R.color.red));

        try{
            setUpUserCardModels();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void setUpUserCardModels() throws JSONException {
        userLogic.getAllUsers(new getAllUsersResponse() {
            @Override
            public void onSuccess(List<UserListCardModel> list) {
                for (int i = 0; i < list.size(); i++){
                    userListCardModel.add(list.get(i));
                    Log.d("all users", list.get(i).getUsername());
                }
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s) {
                Toast.makeText(UserListActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onUserClick(int position) {
        Intent intent = new Intent(UserListActivity.this, IndividualUserActivity.class);
        System.out.print("position: ");
        System.out.println(position);
        System.out.println(userListCardModel.get(position).getEmail());

        intent.putExtra("EMAIL", userListCardModel.get(position).getEmail());
        intent.putExtra("PASSWORD", userListCardModel.get(position).getPassword());
        intent.putExtra("FULLNAME", userListCardModel.get(position).getFullName());
        intent.putExtra("USERNAME", userListCardModel.get(position).getUsername());
        intent.putExtra("PHONENUM", userListCardModel.get(position).getPhoneNum());
        intent.putExtra("DOB", userListCardModel.get(position).getDob());
        intent.putExtra("PHOTOURL", userListCardModel.get(position).getPhotoUrl());

        intent.putExtra("USERTYPE", userListCardModel.get(position).getUserType());

        intent.putExtra("USERID", userListCardModel.get(position).getUserId());

        startActivity(intent);

    }

    private void navigationDrawer() {
        // Navigation Drawer
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_all_users);

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
        if (menuItem.getItemId() != R.id.nav_all_users){
            NavMenuUtils.onNavItemSelected(menuItem, UserListActivity.this);
        }
        else{
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
