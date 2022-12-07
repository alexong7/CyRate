package com.example.cyrate.Logic;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cyrate.AppController;
import com.example.cyrate.Logic.BusinessInterfaces.businessStringResponse;
import com.example.cyrate.Logic.BusinessInterfaces.getBusinessByIDResponse;
import com.example.cyrate.Logic.BusinessInterfaces.getBusinessPostsByID;
import com.example.cyrate.Logic.BusinessInterfaces.getBusinessesResponse;
import com.example.cyrate.Logic.ReviewInterfaces.reviewStringResponse;
import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.BusinessPostCardModel;
import com.example.cyrate.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BusinessServiceLogic {


    /**
     * Makes a request to the server to fetch all the
     * businesses and returns a List of the BusinessListCardModels
     *
     * @param r - Interface used for async callbacks to the BusinessListActivity
     */
    public void getBusinesses(getBusinessesResponse r) throws JSONException {
        List<BusinessListCardModel> businessModelsList = new ArrayList<>();
        Log.d("TEST 1", "IN GETBUSSINESS");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                Const.GET_BUSINESSES_URL, null, response -> {
            Log.d("TEST 1", "IN ON RESPONSE");

            for (int i = 0; i < response.length(); i++) {
                try {
                    // Get each business from the JSON array
                    JSONObject business = (JSONObject) response.get(i);
                    Log.d("JSON OBJ", business.toString());

                    // Some priceGauges are null, lets do a check first
                    String priceGauge = business.get("priceGauge").toString().equals("null") ? "$" : business.get("priceGauge").toString();

                    BusinessListCardModel businessListCardModel = new BusinessListCardModel(
                            (int) business.get("busId"),
                            business.get("busName").toString(),
                            business.get("busType").toString(),
                            business.getString("phone"),
                            business.get("photoUrl").toString(),
                            business.get("hours").toString(),
                            business.get("location").toString(),
                            (int) business.get("ownerId"),
                            business.get("menuLink").toString(),
                            priceGauge,
                            (int) business.get("reviewSum"),
                            (int) business.get("reviewCount")
                    );
                    businessModelsList.add(businessListCardModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Send the businessModelsList back to the BusinessListActivity
            // as a async callback
            r.onSuccess(businessModelsList);
        }, error -> r.onError(error.toString())

        );

        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * Makes a request to the server to GET an individual business
     * by a businessId.
     *
     * @param busId
     * @param r
     * @throws JSONException
     */
    public void getBusinessesById(int busId, getBusinessByIDResponse r) throws JSONException {
        String url = Const.GET_BUSINESS_BY_ID_URL + String.valueOf(busId);
        final BusinessListCardModel[] businessListCardModel = new BusinessListCardModel[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                String priceGauge = response.get("priceGauge").toString().equals("null") ? "$" : response.get("priceGauge").toString();

                businessListCardModel[0] = new BusinessListCardModel(
                        (int) response.get("busId"),
                        response.get("busName").toString(),
                        response.get("busType").toString(),
                        response.getString("phone"),
                        response.get("photoUrl").toString(),
                        response.get("hours").toString(),
                        response.get("location").toString(),
                        (int) response.get("ownerId"),
                        response.get("menuLink").toString(),
                        priceGauge,
                        (int) response.get("reviewSum"),
                        (int) response.get("reviewCount")
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // Send the businessModelsList back to the BusinessListActivity
            // as a async callback
            r.onSuccess(businessListCardModel[0]);
        }, error -> r.onError(error.toString())

        );

        AppController.getInstance().addToRequestQueue(request);
    }


    /**
     * Adds a business to the DB with the given parameters.
     *
     * @param busName
     * @param busType
     * @param busHours
     * @param busLocation
     * @param priceGauge
     * @param photoUrl
     * @param r
     * @throws JSONException
     */
    public void addBusiness(String busName, String busType, String busHours, String busLocation,
                            String priceGauge, String photoUrl, businessStringResponse r) throws JSONException {
        String url = Const.ADD_BUSINESS_URL;

        JSONObject newUserObj = new JSONObject();
        newUserObj.put("busName", busName);
        newUserObj.put("busType", busType);
        newUserObj.put("hours", busHours);
        newUserObj.put("location", busLocation);
        newUserObj.put("priceGauge", priceGauge);
        newUserObj.put("photoUrl", photoUrl);

        // Defaults to fill the required JSON object
        newUserObj.put("ownerId", -1);
        newUserObj.put("menuLink", "");
        newUserObj.put("reviewSum", 0);
        newUserObj.put("reviewCount", 0);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, newUserObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            r.onSuccess(response.toString());
                        } else {
                            r.onError("Null Response object received");
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

    /**
     * Makes a request to the server to DELETE a business given a businessId
     *
     * @param r
     * @param businessId
     * @throws JSONException
     */
    public void deleteBusiness(businessStringResponse r, int businessId) throws JSONException {
        String url = Const.DELETE_BUSINESS_URL + String.valueOf(businessId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                r.onSuccess("Business Deleted");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                r.onError(error.toString());
            }
        }

        );

        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * Makes a request to the server to PUT / update the fields of a business
     * with the given parameters.
     *
     * @param businessId
     * @param busName
     * @param busType
     * @param busHours
     * @param busLocation
     * @param priceGauge
     * @param photoUrl
     * @param r
     * @throws JSONException
     */
    public void editBusiness(int businessId, String busName, String busType,
                             String busHours, String busLocation, String priceGauge, String photoUrl, businessStringResponse r) throws JSONException {
        String url = Const.EDIT_BUSINESS_URL + String.valueOf(businessId);

        // Get the business first in order to get the current ReviewSum and Review Count so that they
        // don't reset to the default values
        this.getBusinessesById(businessId, new getBusinessByIDResponse() {
            @Override
            public void onSuccess(BusinessListCardModel business) {
                JSONObject newUserObj = new JSONObject();
                try {
                    newUserObj.put("busName", busName);
                    newUserObj.put("busType", busType);
                    newUserObj.put("hours", busHours);
                    newUserObj.put("location", busLocation);
                    newUserObj.put("priceGauge", priceGauge);
                    newUserObj.put("photoUrl", photoUrl);
                    newUserObj.put("reviewSum", business.getReviewSum());
                    newUserObj.put("reviewCount", business.getReviewCount());

                    // Defaults to fill the required JSON object
                    newUserObj.put("ownerId", -1);
                    newUserObj.put("menuLink", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                        url, newUserObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        r.onSuccess("Successfully Updated Business!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        r.onError(error.toString());
                    }
                }

                );

                AppController.getInstance().addToRequestQueue(request);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onError(String s) {
                Log.d("editBusiness - getBusinessById - ERROR", s);
            }
        });
    }

    /**
     * Makes a request to the server and updates a Business' Rating and Total Review count
     * by first fetching the business, and then performing the update.
     *
     * @param busId
     * @param ratingUpdate
     * @param reviewCountUpdate
     * @param r
     * @throws JSONException
     */
    public void editRatingAndReviewCount(int busId, int ratingUpdate, int reviewCountUpdate, businessStringResponse r) throws JSONException {
        String url = Const.EDIT_BUSINESS_URL + String.valueOf(busId);


        this.getBusinessesById(busId, new getBusinessByIDResponse() {
            @Override
            public void onSuccess(BusinessListCardModel business) {
                JSONObject obj = new JSONObject();
                try {
                    // Update only the reviewSum and reviewCount
                    obj.put("reviewSum", business.getReviewSum() + ratingUpdate);
                    obj.put("reviewCount", business.getReviewCount() + reviewCountUpdate);

                    // Keep the previous information for the rest
                    obj.put("busName", business.getBusName());
                    obj.put("busType", business.getBusType());
                    obj.put("hours", business.getHours());
                    obj.put("location", business.getLocation());
                    obj.put("priceGauge", business.getPriceGauge());
                    obj.put("photoUrl", business.getPhotoUrl());
                    obj.put("ownerId", -1);
                    obj.put("menuLink", "");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                        url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        r.onSuccess("Updated Rating and Review Count!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        r.onError(error.toString());
                    }
                }

                );

                AppController.getInstance().addToRequestQueue(request);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onError(String s) {
                Log.d("editRatingAndReviewCount - getBusById - ERROR", s);
            }
        });

    }

    /**
     * Makes a request to the server to GET all the Posts made by a Business given
     * a businessId.
     *
     * @param busID
     * @param r
     * @throws JSONException
     */
    public void getBusinessPostsByID(int busID, getBusinessPostsByID r) throws JSONException {
        List<BusinessPostCardModel> businessPostList = new ArrayList<>();
        String url = Const.GET_BUSINESS_POSTS_BY_ID + String.valueOf(busID);
        Log.d("TEST 1", "IN getBusinessPostsByID");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url, null, response -> {
            Log.d("TEST 1", "IN ON RESPONSE");

            for (int i = 0; i < response.length(); i++) {
                try {
                    // Get each business from the JSON array
                    JSONObject post = (JSONObject) response.get(i);
                    Log.d("JSON OBJ", post.toString());

                    JSONObject busJSON = post.getJSONObject("business");
                    BusinessListCardModel bus = new BusinessListCardModel(
                            busJSON.getInt("busId"),
                            busJSON.getString("busName"),
                            busJSON.getString("busType"),
                            busJSON.getString("phone"),
                            busJSON.getString("photoUrl"),
                            busJSON.getString("hours"),
                            busJSON.getString("location"),
                            busJSON.getInt("ownerId"),
                            busJSON.getString("menuLink"),
                            busJSON.getString("priceGauge"),
                            busJSON.getInt("reviewSum"),
                            busJSON.getInt("reviewCount")
                    );

                    BusinessPostCardModel busPostCardModel = new BusinessPostCardModel(
                            post.getInt("pid"),
                            post.getString("postTxt"),
                            post.getString("date"),
                            Base64.decode(post.getString("blobPhoto"), Base64.DEFAULT),
                            bus
//                            1000
                    );
                    businessPostList.add(busPostCardModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Send the businessModelsList back to the BusinessListActivity
            // as a async callback
            r.onSuccess(businessPostList);
        }, error -> r.onError(error.toString())

        );

        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * Makes a request to the server to POST a new Business Post given a businessId and
     * the required Post information.
     *
     * @param busId
     * @param postTxt
     * @param blobPhoto
     * @param r
     * @throws JSONException
     */
    public void addPost(int busId, String postTxt, String blobPhoto, businessStringResponse r) throws JSONException {
        String url = Const.CREATE_POST + String.valueOf(busId);

        Log.d("ADD POST URL", url);

        // Get the current date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        String dateStr = formatter.format(date);

        HashMap<String, Object> params = new HashMap<>();
        params.put("postTxt", postTxt);
        params.put("date", dateStr);
        params.put("blobPhoto", blobPhoto);

        Log.d("addPost - newPost", params.toString());


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        r.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ADD POST ERROR", error.toString());
                        r.onError(error.getMessage());
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * Makes a request to the server to delete a specific Business Post given a PostID.
     *
     * @param postId
     * @param r
     * @throws JSONException
     */
    public void deleteBusinessPost(int postId, businessStringResponse r) throws JSONException {
        String url = Const.DELETE_POST + String.valueOf(postId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                r.onSuccess("Post Deleted");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                r.onError(error.toString());
            }
        }

        );

        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * Makes a request to the server to edit a specific Business Post given a PostID,
     * and the updated fields.
     *
     * @param postId
     * @param postTxt
     * @param photoUrl
     * @param r
     * @throws JSONException
     */
    public void editPost(int postId, String postTxt, String photoUrl, businessStringResponse r) throws JSONException {
        String url = Const.EDIT_POST + String.valueOf(postId);

        Log.d("EDIT POST URL", url);

        // Get the current date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        String dateStr = formatter.format(date);

        HashMap<String, Object> params = new HashMap<>();
        params.put("postTxt", postTxt);
        params.put("date", dateStr);
        params.put("photoUrl", photoUrl);

        Log.d("addPost - newPost", params.toString());


        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        r.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ADD POST ERROR", error.toString());
                        r.onError(error.getMessage());
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

}

