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
import com.example.cyrate.models.ReviewRecyclerViewInterface;
import com.example.cyrate.models.ReviewListCardModel;

import java.util.ArrayList;

/**
 * Adapter to populate the RecyclerView for a list of Reviews, either for
 * a specific business or a user's personal reviews.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {
    private final ReviewRecyclerViewInterface recyclerViewInterface;

    Context ctx;
    ArrayList<ReviewListCardModel> reviewListCardModels;

    /**
     *
     * @param ctx
     * @param reviewListCardModels
     * @param recyclerViewInterface
     */
    public ReviewListAdapter(
            Context ctx,
            ArrayList<ReviewListCardModel> reviewListCardModels,
            ReviewRecyclerViewInterface recyclerViewInterface
    ){
        this.ctx = ctx;
        this.reviewListCardModels = reviewListCardModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ReviewListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.review_list_card, parent, false);
        return new ReviewListAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.MyViewHolder holder, int position) {
        new ImageLoaderTask(reviewListCardModels.get(position).getReviewUser().getPhotoUrl(), holder.profilePic).execute();


        holder.reviewerName.setText(reviewListCardModels.get(position).getReviewUser().getUsername());
        holder.reviewText.setText(reviewListCardModels.get(position).getReviewHeader());
        holder.rateVal.setText(String.valueOf(reviewListCardModels.get(position).getRateVal()));

    }

    @Override
    public int getItemCount() {
        return reviewListCardModels.size();
    }

    // Class necessary and is similar for having an onCreate method. Allows us to get all our views
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView reviewerName, reviewText, rateVal;

        public MyViewHolder(@NonNull View itemView, ReviewRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewText = itemView.findViewById(R.id.review_txt);
            rateVal = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onReviewClick(pos);
                        }
                    }
                }
            });
        }
    }
}
