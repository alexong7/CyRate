package com.example.cyrate.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.Logic.UserInterfaces.editProfileResponse;
import com.example.cyrate.Logic.UserInterfaces.userStringResponse;
import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.R;
import com.example.cyrate.UserType;

import org.json.JSONException;

public class IndividualUserActivity extends AppCompatActivity {
    Button deleteUserBtn, editUserTypeButton;
    ImageView backBtn, profilePic;
    TextView userName, userEmail;
    Spinner userTypeDropdown;

    String email, password, fullName, username, phoneNum, dob, photoUrl;
    UserType userType;
    int userId;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_user);

        extras = getIntent().getExtras();

        //get the extras
        email = extras.getString("EMAIL");
        password = extras.getString("PASSWORD");
        fullName = extras.getString("FULLNAME");
        username = extras.getString("USERNAME");
        phoneNum = extras.getString("PHONENUM");
        dob = extras.getString("DOB");
        photoUrl = extras.getString("PHOTOURL");

        userType = (UserType)(extras.get("USERTYPE"));
        userId = extras.getInt("USERID");

        //get ui elements
        backBtn = (ImageView) findViewById(R.id.back_button_image);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        deleteUserBtn = (Button) findViewById(R.id.btn_delete_user);
        editUserTypeButton = (Button) findViewById(R.id.btn_edit_user);
        userName = (TextView) findViewById(R.id.user_fullname);
        userEmail = (TextView) findViewById(R.id.user_email);
        userTypeDropdown = (Spinner) findViewById(R.id.spinner_user_type);

        //profile pic
        new ImageLoaderTask(photoUrl, profilePic).execute();

        //user type dropdown
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(IndividualUserActivity.this, R.array.userTypesAdmin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        userTypeDropdown.setAdapter(adapter);

        //set default user type
        int selection;
        switch(userType){
            case BUSINESS_OWNER:
                selection = 1;
                break;
            case ADMIN:
                selection = 2;
                break;
            case BASIC_USER:
            default:
                selection = 0;
        }

        userTypeDropdown.setSelection(selection);


        //set user info ui elements
        String nameToDisplay = username;
        //if user's full name isn't empty, display that instead
        if (fullName != null && fullName.length() > 0){
            nameToDisplay = fullName;
        }
        if (nameToDisplay == null || nameToDisplay.length() == 0){
            nameToDisplay = "no name available";
        }

        System.out.println("username: " + username + " to display: " + nameToDisplay);
        userName.setText(nameToDisplay);
        userEmail.setText(email);

        //deal with button clicks
        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete user
                UserLogic userLogic = new UserLogic();

                try{
                    userLogic.deleteUser(new userStringResponse() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(IndividualUserActivity.this, s, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(IndividualUserActivity.this, UserListActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onError(String s) {
                            Toast.makeText(IndividualUserActivity.this, s, Toast.LENGTH_LONG).show();

                        }
                    }, userId);
                } catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });

        editUserTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogic userLogic = new UserLogic();

                //get new type
                String newUserType = userTypeDropdown.getSelectedItem().toString();

                try{
                    userLogic.editUser(userId, username, email, password, fullName, dob, photoUrl, phoneNum, newUserType, new editProfileResponse() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(IndividualUserActivity.this, "User Updated", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(String s) {
                            Toast.makeText(IndividualUserActivity.this, s, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IndividualUserActivity.this, UserListActivity.class);
                startActivity(i);
            }
        });
    }
}
