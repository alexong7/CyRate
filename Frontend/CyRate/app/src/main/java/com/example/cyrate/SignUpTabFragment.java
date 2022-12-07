package com.example.cyrate;

import static com.example.cyrate.activities.IntroActivity.emailPasswordMap;
import static com.example.cyrate.activities.IntroActivity.phoneNumberSet;
import static com.example.cyrate.activities.IntroActivity.usernamesSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;
import com.example.cyrate.Logic.FavoritesServiceLogic;
import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.Logic.UserInterfaces.addUserResponse;
import com.example.cyrate.Logic.UserInterfaces.getUserByEmailResponse;
import com.example.cyrate.activities.BusinessListActivity;
import com.example.cyrate.activities.IntroActivity;
import com.example.cyrate.activities.MainActivity;
import com.example.cyrate.activities.WelcomeToCyRateActivity;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.UserModel;

import org.json.JSONException;

import java.util.List;

public class SignUpTabFragment extends Fragment {
    public static boolean keepChecking = false;

    EditText email, password, confirmPassword, username, phoneNumber, dateOfBirth;
    Button signUp;
    Spinner userTypeSpinner;
    String selectedUserType = "normal";

    FavoritesServiceLogic favoritesServiceLogic;

    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_fragment, container, false);

        favoritesServiceLogic = new FavoritesServiceLogic();


        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirmPassword = root.findViewById(R.id.confirmPassword);
        signUp = root.findViewById(R.id.btn_signUp);
        username = root.findViewById(R.id.username);
        phoneNumber = root.findViewById(R.id.phoneNumber);
        userTypeSpinner = root.findViewById(R.id.user_type);


        email.setTranslationX(800);
        password.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        signUp.setTranslationX(800);
        username.setTranslationX(800);
        phoneNumber.setTranslationX(800);
        userTypeSpinner.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        confirmPassword.setAlpha(v);
        signUp.setAlpha(v);
        username.setAlpha(v);
        phoneNumber.setAlpha(v);
        userTypeSpinner.setAlpha(v);


        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signUp.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1500).start();
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        phoneNumber.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        userTypeSpinner.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(230).start();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.userTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        userTypeSpinner.setAdapter(adapter);

        signUp.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerUser(view);
                    }
                }));

        return root;
    }

    public void registerUser(View view) {

        // Check if any inputs are not field
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty() ||
//                username.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty() || dateOfBirth.getText().toString().isEmpty()
                username.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty()
        ) {
            Toast.makeText(getActivity(), "Complete All Inputs", Toast.LENGTH_LONG).show();
        } else {

            selectedUserType = userTypeSpinner.getSelectedItem().toString();
            selectedUserType = selectedUserType == "Normal" ? "normal" : "owner";

            //get the data from the textboxes
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            String userConfirmPassword = confirmPassword.getText().toString();
            String userUsername = username.getText().toString();
            String userPhoneNumber = phoneNumber.getText().toString();
            String userDateOfBirth = "";
            String userType = selectedUserType;


            UserLogic userLogic = new UserLogic();


            //look for email in database (GET user by email)

            userLogic.getUserByEmail(userEmail, new getUserByEmailResponse() {
                        @Override
                        public void onSuccess(UserModel userModel) {
                            Toast.makeText(getActivity(), "sorry, this email is already in use", Toast.LENGTH_LONG).show();
                            keepChecking = false;

                            return;
                        }

                        @Override
                        public void onError(String s) {
                            Log.d("SUCCESS", "In onERROR (didnt find email)");
                            keepChecking = true;

                            //confirm userPassword and userConfirmPassword are equal
                            if (keepChecking) {
                                if (!userPassword.equals(userConfirmPassword)) {
                                    Log.d("USERPASS", "Passwords Don't match");
                                    Toast.makeText(getActivity(), "oops! passwords don't match!", Toast.LENGTH_SHORT).show();
                                    keepChecking = false;
                                    return;
                                }
                            }

                            // Check if username already used
                            if (keepChecking) {
                                Log.d("SIGNUP TAB FRAG", IntroActivity.usernamesSet.toString());
                                if (IntroActivity.usernamesSet.contains(userUsername) || userUsername == null) {
                                    Log.d("USERNAME", "Username exists");
                                    keepChecking = false;
                                    Toast.makeText(getActivity(), "username is unavailable", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            // Check if phone number already used
                            if (keepChecking) {
                                Log.d("SIGNUP TAB FRAG", IntroActivity.phoneNumberSet.toString());
                                if (IntroActivity.phoneNumberSet.contains(userPhoneNumber) || userPhoneNumber == null) {
                                    Log.d("PHONE NUMBER", "Phone Number exists");
                                    keepChecking = false;
                                    Toast.makeText(getActivity(), "Phone number already used!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            if (keepChecking) {
                                try {
                                    userLogic.addUser(new addUserResponse() {

                                        @Override
                                        public void onSuccess(String s) {
                                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                                            userLogic.getUserByEmail(userEmail, new getUserByEmailResponse() {
                                                @Override
                                                public void onSuccess(UserModel userModel) {
                                                    //set global user
                                                    MainActivity.globalUser = userModel;
                                                    favoritesServiceLogic.getFavoritesByUser(MainActivity.globalUser.getUserId(), new getBusinessesResponse() {
                                                        @Override
                                                        public void onSuccess(List<BusinessListCardModel> list) {
                                                            Log.d("favs", "successful");
                                                        }

                                                        @Override
                                                        public void onError(String s) {
                                                            Log.d("favs", "error: " + s);
                                                        }
                                                    });

                                                    Intent i;
                                                    if (MainActivity.globalUser.getUserType() == UserType.BASIC_USER) {
                                                        i = new Intent(getActivity(), WelcomeToCyRateActivity.class);
                                                    }
                                                    else{
                                                        i = new Intent(getActivity(), BusinessListActivity.class);
                                                    }
                                                    startActivity(i);
                                                }

                                                @Override
                                                public void onError(String s) {
                                                    Log.d("ERROR", "Error in getUserByEmail -> " + s);
                                                    Toast.makeText(getActivity(), "oops: " + s, Toast.LENGTH_LONG).show();

                                                }
                                            });

                                        }

                                        @Override
                                        public void onError(String s) {
                                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                                            keepChecking = false;
                                        }
                                    }, userType, userEmail, userPassword, userUsername, userPhoneNumber, userDateOfBirth);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
            );

            //if we were successful, add to hashmap and sets
            if (keepChecking){
                addUserInfoToInstance(userEmail, userPassword, userUsername, userPhoneNumber);
            }
        }
    }

    private void addUserInfoToInstance(String email, String password, String username, String phoneNum){
        emailPasswordMap.put(email, password);
        usernamesSet.add(username);
        phoneNumberSet.add(phoneNum);
        return;
    }
}
