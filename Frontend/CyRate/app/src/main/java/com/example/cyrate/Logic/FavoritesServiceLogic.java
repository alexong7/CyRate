package com.example.cyrate.Logic;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cyrate.AppController;
import com.example.cyrate.Logic.BusinessInterfaces.businessStringResponse;
import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;
import com.example.cyrate.activities.IntroActivity;
import com.example.cyrate.activities.MainActivity;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.net_utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoritesServiceLogic {

    public void addFavorite(int userId, int businessId, businessStringResponse r) throws JSONException {
        String url = Const.ADD_FAVORITE_URL + String.valueOf(businessId) + "/user/" + String.valueOf(userId);

        JSONObject newFavoriteObject = new JSONObject();

        Log.d("in add fav", "HERE");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, newFavoriteObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fav service logic", "response.toString()");
                        if (response != null) {
                            r.onSuccess(response.toString());
                        } else {
                            r.onError("Null response object received");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        r.onError(error.getMessage());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);

    }

    public void getFavoritesByUser(int uid, getBusinessesResponse r) {
        String url = Const.GET_FAVORITES_BY_USER_URL + String.valueOf(uid);
        ArrayList<BusinessListCardModel> favoriteBusinesses = new ArrayList<>();
        HashMap<Integer, Integer> newFidToBid = new HashMap<Integer, Integer>();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    Log.d("favs", "in try");
                    Log.d("favs obj", response.get(i).toString());
                    //get each business
                    BusinessListCardModel newFavorite;
                    JSONObject row = (JSONObject) response.get(i);
                    JSONObject business = row.getJSONObject("business");
                    Log.d("favs", business.toString());
                    Log.d("favs", "busId: " + business.getInt("busId") + "   /   " + business.getString("busId"));

                    int fid = row.getInt("fid");
                    Log.d("fid", String.valueOf(fid));
                    int busId = business.getInt("busId");
                    String busName = business.getString("busName");
                    String busType = business.getString("busType");
                    String phoneNum = business.getString("phone");
                    String photoUrl = business.getString("photoUrl");
                    String hours = business.getString("hours");
                    String location = business.getString("location");
                    int ownerId = business.getInt("ownerId");
                    String menuLink = business.getString("menuLink");
                    String priceGauge = business.getString("priceGauge");
                    int reviewSum = business.getInt("reviewSum");
                    int reviewCount = business.getInt("reviewCount");

                    newFavorite = new BusinessListCardModel(busId, busName, busType, phoneNum, photoUrl, hours, location, ownerId, menuLink, priceGauge, reviewSum, reviewCount);
                    newFavorite.setFid(fid);

                    Log.d("favs", busName);
                    favoriteBusinesses.add(newFavorite);

                    newFidToBid.put(row.getInt("fid"), business.getInt("busId"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            MainActivity.fidToBid = newFidToBid;
            r.onSuccess(favoriteBusinesses);
        }, error -> r.onError(error.toString())
        );

        AppController.getInstance().addToRequestQueue(arrayRequest);
    }

    public void deleteFavorite(businessStringResponse r, int businessId) throws JSONException {
        String url = Const.DELETE_FAVORITE_BY_ID_URL + String.valueOf(businessId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                r.onSuccess("Removed from Favorites");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                r.onError(error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}