package com.example.cyrate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyrate.ImageLoaderTask;
import com.example.cyrate.R;
import com.example.cyrate.models.CommentThreadCardModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CommentThreadAdapter extends RecyclerView.Adapter<CommentThreadAdapter.ViewHolder> {

    Context ctx;
    ArrayList<CommentThreadCardModel> commentListCardModels;

    public CommentThreadAdapter(
            Context ctx,
            ArrayList<CommentThreadCardModel> commentListCardModels
    ) {
        this.ctx = ctx;
        this.commentListCardModels = commentListCardModels;
    }

    @NonNull
    @Override
    public CommentThreadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.comment_thread_card, parent, false);
        return new CommentThreadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.threadBar.setVisibility(View.VISIBLE);

        String displayName = commentListCardModels.get(position).getName();

        if(displayName == null || displayName.isEmpty()){
            displayName = "Anonymous User";
        }

        new ImageLoaderTask(commentListCardModels.get(position).getPhotoUrl(), holder.profilePic).execute();
        holder.commenterName.setText(displayName);
        holder.commentText.setText(commentListCardModels.get(position).getCommentBody());
        holder.date.setText(String.valueOf(commentListCardModels.get(position).getDate()));

        if(position == getItemCount() - 1){
            holder.threadBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return commentListCardModels.size();
    }

    // Class necessary and is similar for having an onCreate method. Allows us to get all our views
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView commenterName, commentText, date;
        View threadBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.commentThread_profilePic);
            commenterName = itemView.findViewById(R.id.commentThread_name);
            commentText = itemView.findViewById(R.id.commentThread_bodyText);
            date = itemView.findViewById(R.id.commentThread_date);

            threadBar = itemView.findViewById(R.id.threadBar);

        }
    }

    public void addItem(JSONObject obj) {
        try {
            CommentThreadCardModel commentModel = new CommentThreadCardModel(
                    obj.getString("commenterName"),
                    obj.getString("photoUrl"),
                    obj.getString("commentBody"),
                    obj.getString("date")
            );

            commentListCardModels.add(commentModel);
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
