package com.example.cyrate.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyrate.Logic.UserInterfaces.getAllUsersResponse;
import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.NavMenuUtils;
import com.example.cyrate.R;
import com.example.cyrate.adapters.UserListAdapter;
import com.example.cyrate.models.UserListCardModel;
import com.example.cyrate.models.UserModel;
import com.example.cyrate.models.UserRecyclerViewInterface;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserRecyclerViewInterface {

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

    }
}
