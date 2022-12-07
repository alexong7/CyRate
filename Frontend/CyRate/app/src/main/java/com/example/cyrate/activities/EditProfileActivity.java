package com.example.cyrate.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.Logic.UserInterfaces.editProfileResponse;
import com.example.cyrate.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //declare variables
    EditText usernameText;
    EditText emailText;
    EditText passwordText;
    EditText nameText;
    EditText birthdayText;
    EditText photoUrlText;
    Button updateButton;

    UserLogic userLogic;


//    String newName;
//    String newUsername;
//    String newBirthday;
//    String newPassword;
//    String newEmail;
//    String newPhoto;
//    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //set declared variables
        usernameText = findViewById(R.id.edittext_username);
        emailText = findViewById(R.id.edittext_email);
        passwordText = findViewById(R.id.edittext_password);
        nameText = findViewById(R.id.edittext_name);
        birthdayText = findViewById(R.id.edittext_birthday);
        photoUrlText = findViewById(R.id.edittext_photo_url);
        updateButton = findViewById(R.id.btn_update);

        //name isn't filled in during registration
        String currentName = MainActivity.globalUser.getFullName();
        String nameHint;
        nameHint = currentName != null && currentName.length() > 0 ? currentName : "Name";

        usernameText.setHint(MainActivity.globalUser.getUsername());
        emailText.setHint(MainActivity.globalUser.getEmail());
        passwordText.setHint(MainActivity.globalUser.getPassword());
        nameText.setHint(nameHint);
        birthdayText.setHint(MainActivity.globalUser.getDob());
        photoUrlText.setHint(MainActivity.globalUser.getPhotoUrl());

        updateButton.setOnClickListener((new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                userLogic = new UserLogic();

                String newUsername = usernameText.getText().toString();
                String newEmail = emailText.getText().toString();
                String newPassword = passwordText.getText().toString();
                String newName = nameText.getText().toString();
                String newBirthday = birthdayText.getText().toString();
                String newPhoto = photoUrlText.getText().toString();
                String phoneNum = MainActivity.globalUser.getPhoneNum();
                int id = MainActivity.globalUser.getUserId();

                //check if anything is empty. if it is, fill with current contents of user
                newUsername = !newUsername.isEmpty() ? newUsername : MainActivity.globalUser.getUsername();
                newEmail = !newEmail.isEmpty() ? newEmail : MainActivity.globalUser.getEmail();
                newPassword = !newPassword.isEmpty() ? newPassword : MainActivity.globalUser.getPassword();
                newName = !newName.isEmpty() ? newName : MainActivity.globalUser.getFullName();
                newBirthday = !newBirthday.isEmpty() ? newName : MainActivity.globalUser.getDob();
                newPhoto = !newPhoto.isEmpty() ? newPhoto : MainActivity.globalUser.getPhotoUrl();

                //update
                try{
                    userLogic.editUser(id, newUsername, newEmail, newPassword, newName, newBirthday, newPhoto, phoneNum, MainActivity.globalUser.getUserType().toString(), new editProfileResponse() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(EditProfileActivity.this, s, Toast.LENGTH_LONG);
                            //set fields back to empty
                            usernameText.setText("");
                            emailText.setText("");
                            passwordText.setText("");
                            nameText.setText("");
                            birthdayText.setText("");
                            photoUrlText.setText("");

                        }

                        @Override
                        public void onError(String s) {
                            Toast.makeText(EditProfileActivity.this, s, Toast.LENGTH_LONG);
                        }
                    });
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
