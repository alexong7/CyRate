package com.example.cyrate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;
import com.example.cyrate.Logic.FavoritesServiceLogic;
import com.example.cyrate.Logic.UserLogic;
import com.example.cyrate.Logic.UserInterfaces.getUserByEmailResponse;
import com.example.cyrate.activities.BusinessListActivity;
import com.example.cyrate.activities.IntroActivity;
import com.example.cyrate.activities.MainActivity;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.UserModel;
import com.example.cyrate.activities.WelcomeToCyRateActivity;

import java.util.List;

public class LoginTabFragment extends Fragment {

    EditText email, password;
    TextView forgotPass;
    Button login, continueAsGuest;

    String userEmail;
    String userPassword;

    FavoritesServiceLogic favoritesServiceLogic;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        forgotPass = root.findViewById(R.id.forgotPassword);
        login = root.findViewById(R.id.btn_login);
        continueAsGuest = root.findViewById(R.id.btn_use_as_guest);

        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPass.setTranslationX(800);
        login.setTranslationX(800);
        continueAsGuest.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forgotPass.setAlpha(v);
        login.setAlpha(v);
        continueAsGuest.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        continueAsGuest.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        favoritesServiceLogic = new FavoritesServiceLogic();

        login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get email and password
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                //get user by email

                String expectedPassword = IntroActivity.emailPasswordMap.get(userEmail);

                if (IntroActivity.emailPasswordMap.get(userEmail) != null) {
                    if (userPassword.equals(expectedPassword)){
                        //set global user
                        setGlobalUserAndNavigate(userEmail);
                    }
                    else{
                        //password is wrong - make a toast
                        Toast.makeText(getActivity(), "Password is incorrect", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    //email doesnt exist - make a toast
                    Toast.makeText(getActivity(), "Email is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        }));

        continueAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGlobalUserAndNavigate("guest-user-email");
            }
        });

        return root;

    }

    public void setGlobalUserAndNavigate(String email){
        UserLogic userLogic = new UserLogic();

        userLogic.getUserByEmail(email, new getUserByEmailResponse() {
            @Override
            public void onSuccess(UserModel userModel) {
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
                Log.d("ERROR", "in login");
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}
