package com.example.cyrate;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cyrate.activities.AddBusinessActivity;
import com.example.cyrate.activities.BusinessListActivity;
import com.example.cyrate.activities.EditProfileActivity;
import com.example.cyrate.activities.LoginActivity;
import com.example.cyrate.activities.MainActivity;

public class nav_menu_utils {
    public static void hideMenuItems(Menu navMenu){
        //set visibility of all menu items every time in case users log out and log back in?
            // A guest user should not be able to edit the guest user profile
            if (MainActivity.globalUser.getUserType() == UserType.GUEST){
                navMenu.findItem(R.id.nav_edit_profile).setVisible(false);

                //guest cannot add business
                navMenu.findItem(R.id.nav_addBusiness).setVisible(false);

                //guest cannot log out
                navMenu.findItem(R.id.nav_logout).setVisible(false);

                //guest cannot see their profile
                navMenu.findItem(R.id.nav_profile).setVisible(false);

                //guest CAN sign in
                navMenu.findItem(R.id.nav_sign_in).setVisible(true);

            }

            else if (MainActivity.globalUser.getUserType() == UserType.BASIC_USER){
                //normal user can edit their profile
                navMenu.findItem(R.id.nav_edit_profile).setVisible(true);

                //normal user cannot add business
                navMenu.findItem(R.id.nav_addBusiness).setVisible(false);

                //normal user can log out
                navMenu.findItem(R.id.nav_logout).setVisible(true);

                //normal user can see their profile
                navMenu.findItem(R.id.nav_profile).setVisible(true);

                //normal user cannot sign in
                navMenu.findItem(R.id.nav_sign_in).setVisible(false);
            }

            else if (MainActivity.globalUser.getUserType() == UserType.BUSINESS_OWNER){
                //business owner can edit their profile
                navMenu.findItem(R.id.nav_edit_profile).setVisible(true);

                //business owner can add business
                navMenu.findItem(R.id.nav_addBusiness).setVisible(true);

                //business owner can log out
                navMenu.findItem(R.id.nav_logout).setVisible(true);

                //business owner can see their profile
                navMenu.findItem(R.id.nav_profile).setVisible(true);

                //business owner cannot sign in
                navMenu.findItem(R.id.nav_sign_in).setVisible(false);
            }
    }

    public static boolean onNavItemSelected(MenuItem menuItem, android.content.Context context){
        Intent i;
        switch (menuItem.getItemId()) {
            case R.id.nav_restaurants:
                i = new Intent(context, BusinessListActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_addBusiness:
                i = new Intent(context, AddBusinessActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_profile:
                // code here
                break;
            case R.id.nav_edit_profile:
                i = new Intent(context, EditProfileActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_sign_in:
                i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
            case R.id.nav_logout:
                i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
        }


        return true;
    }
}
