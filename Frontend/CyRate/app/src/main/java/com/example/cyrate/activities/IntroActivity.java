package com.example.cyrate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.Logic.UserInterfaces.getEmailPasswordResponse;
import com.example.cyrate.Logic.UserInterfaces.getUsernamesResponse;
import com.example.cyrate.R;
import com.example.cyrate.models.BusinessListCardModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class IntroActivity extends AppCompatActivity {

    ImageView background, logo;
    TextView welcomeTxt, cyrateTxt;
    LottieAnimationView lottieAnimationView;

    UserLogic userLogic;

    public static HashMap<String, String> emailPasswordMap;
    public static HashSet<String> usernamesSet;
    public static HashSet<String> phoneNumberSet;

    public static ArrayList<BusinessListCardModel> globalUserFavorites;

    private static final int NUM_PAGES = 2;
//    private ViewPager viewPager;
//    private ScreenSlidePageAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final Handler handler = new Handler();

        initializeEmailPasswordMap();
        initializeUsernameSet();
        initializePhoneNumberSet();

        background = findViewById(R.id.bg_image);
        logo = findViewById(R.id.cy_logo);
        welcomeTxt = findViewById(R.id.txt_welcome);
        cyrateTxt = findViewById(R.id.txt_cyrate);
        lottieAnimationView = findViewById(R.id.cycloneAnimation);

//        viewPager = findViewById(R.id.viewPager);
//        pagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);

        background.animate().translationY(-4000).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(3000).setDuration(1000).setStartDelay(4000);
        welcomeTxt.animate().translationY(3000).setDuration(1000).setStartDelay(4000);
        cyrateTxt.animate().translationY(3000).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(3000).setDuration(1000).setStartDelay(4000);


        Intent intent = new Intent(this, LoginActivity.class);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 5000);

    }

    public void initializeEmailPasswordMap(){
        userLogic = new UserLogic();

        userLogic.getAllEmailPassword(new getEmailPasswordResponse() {
            @Override
            public void onSuccess(HashMap<String, String> emailPassword) {
                Log.d("Success", "Hash Map initalized");
                emailPasswordMap = emailPassword;
                Log.d("Success", emailPasswordMap.toString());
            }

            @Override
            public void onError(String s) {
                Log.d("ERROR IN Intro Act", s);

            }
        });
    }

    public void initializeUsernameSet(){
        userLogic = new UserLogic();

        userLogic.getAllUsernames(new getUsernamesResponse() {
            @Override
            public void onSuccess(HashSet<String> usernames) {
                Log.d("Success", "Hash Set initalized");
                usernamesSet = usernames;
                Log.d("Success", usernames.toString());
            }

            @Override
            public void onError(String s) {
                Log.d("ERROR IN Intro Act", s);
            }
        });
    }

    public void initializePhoneNumberSet(){
        userLogic = new UserLogic();

        userLogic.getAllPhoneNumbers(new getUsernamesResponse() {
            @Override
            public void onSuccess(HashSet<String> phoneNumber) {
                Log.d("Success", "Hash Set initalized");
                phoneNumberSet = phoneNumber;
                Log.d("Success", phoneNumber.toString());
            }

            @Override
            public void onError(String s) {
                Log.d("ERROR IN Intro Act", s);
            }
        });
    }

//    private class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {
//
//        public ScreenSlidePageAdapter(@NonNull FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return new LoginTabFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//    }
}

