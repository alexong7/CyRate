package com.example.cyrate;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cyrate.activities.AddBusinessActivity;
import com.example.cyrate.activities.BusinessListActivity;
import com.example.cyrate.activities.EditProfileActivity;
import com.example.cyrate.activities.FavoritesActivity;
import com.example.cyrate.activities.LoginActivity;
import com.example.cyrate.activities.MainActivity;
import com.example.cyrate.activities.UserListActivity;
import com.example.cyrate.activities.WelcomeToCyRateActivity;
import com.example.cyrate.activities.PersonalReviewListActivity;
import com.example.cyrate.models.UserModel;

public class NavMenuUtils {
    /**
     * Utility for hiding menu items based on user authentication class level
     * @param navMenu
     */
    public static void hideMenuItems(Menu navMenu){
        //set visibility of all menu items every time in case users log out and log back in?
            // A guest user should not be able to edit the guest user profile

            if(MainActivity.globalUser == null){
                Log.d("GLOBAL USER", "Global User NULL");
                // Fallback, mainly for  testing purposes
                MainActivity.globalUser = new UserModel("TempEmail", "TempPass");
                MainActivity.globalUser.setUserType(UserType.GUEST);
            }
            if (MainActivity.globalUser.getUserType() == UserType.GUEST){
                navMenu.findItem(R.id.nav_edit_profile).setVisible(false);

                //guest cannot add business
                navMenu.findItem(R.id.nav_addBusiness).setVisible(false);

                //guest cannot log out
                navMenu.findItem(R.id.nav_logout).setVisible(false);

                //guest CAN sign in
                navMenu.findItem(R.id.nav_sign_in).setVisible(true);

                //guest cannot see home (bc they don't have personalized info)
                navMenu.findItem(R.id.nav_home).setVisible(false);

                //guest CANNOT see their own reviews
                navMenu.findItem(R.id.nav_my_reviews).setVisible(false);

                //guest CANNOT see user list
                navMenu.findItem(R.id.nav_all_users).setVisible(false);

                //guest CANNOT see favorites
                navMenu.findItem(R.id.nav_favorites).setVisible(false);

            }

            else if (MainActivity.globalUser.getUserType() == UserType.BASIC_USER){
                //normal user can edit their profile
                navMenu.findItem(R.id.nav_edit_profile).setVisible(true);

                //normal user cannot add business
                navMenu.findItem(R.id.nav_addBusiness).setVisible(false);

                //normal user can log out
                navMenu.findItem(R.id.nav_logout).setVisible(true);

                //normal user cannot sign in
                navMenu.findItem(R.id.nav_sign_in).setVisible(false);

                //normal user can see home
                navMenu.findItem(R.id.nav_home).setVisible(true);

                //normal user can see their own reviews
                navMenu.findItem(R.id.nav_my_reviews).setVisible(true);

                //normal user CANNOT see user list
                navMenu.findItem(R.id.nav_all_users).setVisible(false);

            }

            else if (MainActivity.globalUser.getUserType() == UserType.BUSINESS_OWNER){
                //business owner can edit their profile
                navMenu.findItem(R.id.nav_edit_profile).setVisible(true);

                //business owner can add business
                navMenu.findItem(R.id.nav_addBusiness).setVisible(true);

                //business owner can log out
                navMenu.findItem(R.id.nav_logout).setVisible(true);

                //business owner cannot sign in
                navMenu.findItem(R.id.nav_sign_in).setVisible(false);

                //business owner cannot see home (they can't have favorites, leave reviews)
                navMenu.findItem(R.id.nav_home).setVisible(false);

                //business owner CANNOT see their own reviews
                navMenu.findItem(R.id.nav_my_reviews).setVisible(false);

                //business owner CANNOT see user list
                navMenu.findItem(R.id.nav_all_users).setVisible(false);
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
            case R.id.nav_edit_profile:
                i = new Intent(context, EditProfileActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_sign_in:
            case R.id.nav_logout:
                i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_my_reviews:
                i = new Intent(context, PersonalReviewListActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_all_users:
                i = new Intent(context, UserListActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_home:
            default:
                i = new Intent(context, WelcomeToCyRateActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_favorites:
                i = new Intent(context, FavoritesActivity.class);
                context.startActivity(i);
        }


        return true;
    }
}
