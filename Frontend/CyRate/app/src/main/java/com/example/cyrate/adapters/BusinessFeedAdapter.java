package com.example.cyrate.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyrate.EditBusinessPostActivity;
import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.Logic.BusinessInterfaces.businessStringResponse;
import com.example.cyrate.Logic.BusinessServiceLogic;
import com.example.cyrate.Logic.ReviewInterfaces.reviewStringResponse;
import com.example.cyrate.R;
import com.example.cyrate.UserType;
import com.example.cyrate.activities.IndividualReviewActivity;
import com.example.cyrate.activities.MainActivity;
import com.example.cyrate.activities.ReviewListActivity;
import com.example.cyrate.models.BusinessPostCardModel;
//import com.example.cyrate.models.RecyclerViewInterface;
import com.example.cyrate.models.ReviewListCardModel;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to populate the RecyclerView for the list of Posts by a
 * specific business
 */
public class BusinessFeedAdapter extends RecyclerView.Adapter<BusinessFeedAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<BusinessPostCardModel> businessPostList;
    Bundle extras;

    /**
     * @param ctx
     * @param businessPostList
     * @param extras
     */
    public BusinessFeedAdapter(
            Context ctx,
            ArrayList<BusinessPostCardModel> businessPostList,
            Bundle extras
    ) {
        this.ctx = ctx;
        this.businessPostList = businessPostList;
        this.extras = extras;
    }

    @NonNull
    @Override
    public BusinessFeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.business_post_card, parent, false);
        return new BusinessFeedAdapter.MyViewHolder(view, ctx, extras, businessPostList);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessFeedAdapter.MyViewHolder holder, int position) {
        new ImageLoaderTask(businessPostList.get(position).getBusiness().getPhotoUrl(), holder.busProfilePic).execute();
        new ImageLoaderTask(businessPostList.get(position).getPhotoUrl(), holder.busPostPhoto).execute();

        holder.busName.setText(businessPostList.get(position).getBusiness().getBusName());
        holder.busPostDate.setText(businessPostList.get(position).getDate());
        holder.busPostText.setText(businessPostList.get(position).getPostTxt());
        holder.likeCount.setText(String.valueOf(businessPostList.get(position).getLikeCount()));
    }

    @Override
    public int getItemCount() {
        return businessPostList.size();
    }

    // Class necessary and is similar for having an onCreate method. Allows us to get all our views
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView busProfilePic, busPostPhoto, deleteIcon, editIcon, likeButton;
        TextView busName, busPostDate, busPostText, likeCount;

        WebSocketClient webSocket;
        String SERVER_PATH = "wss://ws.postman-echo.com/raw/";

        public MyViewHolder(@NonNull View itemView, Context ctx, Bundle extras, ArrayList<BusinessPostCardModel> list) {
            super(itemView);

            busProfilePic = itemView.findViewById(R.id.busProfilePic);
            busPostPhoto = itemView.findViewById(R.id.busPost_photo);
            busName = itemView.findViewById(R.id.busPost_name);
            busPostDate = itemView.findViewById(R.id.busPost_date);
            busPostText = itemView.findViewById(R.id.busPost_bodyText);
            likeCount = itemView.findViewById(R.id.busPost_likeCount);

            deleteIcon = itemView.findViewById(R.id.busPost_deleteIcon);
            editIcon = itemView.findViewById(R.id.busPost_editIcon);
            likeButton = itemView.findViewById(R.id.busPost_thumbsUp);

            // Remove the delete icon if the current User is not the original reviewer or not an Admin
            deleteIcon.setVisibility(View.GONE);
            editIcon.setVisibility(View.GONE);


            // Update the thumbsUpIcon and CommentIcon position since we removed the deleteIcon
            ConstraintLayout cl = (ConstraintLayout) itemView.findViewById(R.id.busCard_constraintLayout);
            ConstraintSet cs = new ConstraintSet();
            cs.clone(cl);

            cs.setHorizontalBias(R.id.busPost_thumbsUp, (float) 0.4);
            cs.setHorizontalBias(R.id.busPost_comment, (float) 0.6);
            cs.applyTo(cl);

            if (MainActivity.globalUser.getUserType() == UserType.ADMIN) {

                cs.setHorizontalBias(R.id.busPost_thumbsUp, (float) 0.2);
                cs.setHorizontalBias(R.id.busPost_comment, (float) 0.4);
                cs.applyTo(cl);

                deleteIcon.setVisibility(View.VISIBLE);
                editIcon.setVisibility(View.VISIBLE);
            }

            try {
                initiateSocketConnection(ctx);
                webSocket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, EditBusinessPostActivity.class);
                    intent.putExtras(extras);
                    intent.putExtra("POST_TEXT", list.get(getAdapterPosition()).getPostTxt());
                    intent.putExtra("POST_PHOTO", list.get(getAdapterPosition()).getPhotoUrl());
                    intent.putExtra("POST_ID", list.get(getAdapterPosition()).getPostId());

                    ctx.startActivity(intent);
                }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int updatedLikeCount = list.get(getAdapterPosition()).getLikeCount() + 1;
                    likeCount.setText(String.valueOf(updatedLikeCount));
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("POST_ID", list.get(getAdapterPosition()).getPostId());
                        obj.put("LIKE_COUNT", updatedLikeCount);
                        webSocket.send(obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    likeButton.setColorFilter(Color.parseColor("#C20000"));
                }
            });

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BusinessServiceLogic businessServiceLogic = new BusinessServiceLogic();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                    builder.setMessage("Are you sure you want to delete this post?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        businessServiceLogic.deleteBusinessPost(list.get(getAdapterPosition()).getPostId(),
                                                new businessStringResponse() {
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        Toast.makeText(ctx,
                                                                "Successfully Deleted Post!", Toast.LENGTH_LONG).show();

                                                        final Handler handler = new Handler();


                                                        Intent intent = new Intent(ctx, ctx.getClass());
                                                        intent.putExtras(extras);

                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ctx.startActivity(intent);
                                                            }
                                                        }, 800);
                                                    }

                                                    @Override
                                                    public void onError(String s) {
                                                        Log.d("DELETE REVIEW ERROR", s);
                                                        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        }


        private void initiateSocketConnection(Context ctx) throws URISyntaxException {
            Draft[] drafts = {
                    new Draft_6455()
            };


            Log.d("Socket:", "Trying socket");
            webSocket = new WebSocketClient(new URI(SERVER_PATH)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onMessage(String message) {
                Log.d("Socket:" ,"Message Received " + message);
                // Do stuff when server is setup
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {

                }
            };
        }

    }


}
