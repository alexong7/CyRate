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
import com.example.cyrate.UserType;
import com.example.cyrate.models.UserListCardModel;
import com.example.cyrate.models.UserRecyclerViewInterface;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{
    private final UserRecyclerViewInterface recyclerViewInterface;
    Context ctx;
    ArrayList<UserListCardModel> userCardList;

    public UserListAdapter(Context ctx,
                           ArrayList<UserListCardModel> userCardList,
                           UserRecyclerViewInterface recyclerViewInterface) {
        this.ctx = ctx;
        this.userCardList = userCardList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.user_list_card, parent, false);
        return new UserListAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position){
        String userName = userCardList.get(position).getUsername();
        String userEmail = userCardList.get(position).getEmail();
        UserType userType = userCardList.get(position).getUserType();

        String userPhotoUrl = userCardList.get(position).getPhotoUrl();


        if (userPhotoUrl == null || userPhotoUrl.length() == 0 || userPhotoUrl.equals("null")){
            userPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTyK6VC1UacbsCU2hKSWCItKSrh1hRq35XDKg&usqp=CAU";
        }


        new ImageLoaderTask(userPhotoUrl, holder.profilePic).execute();

        holder.userName.setText(userName);
        holder.userEmail.setText(userEmail);
        holder.userType.setText(userType.toString());
    }

    @Override
    public int getItemCount() {
        return userCardList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName, userEmail, userType;

        public MyViewHolder(@NonNull View itemView, UserRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_img);
            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email);
            userType = itemView.findViewById(R.id.user_type);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(recyclerViewInterface != null){
                                int pos = getAdapterPosition();

                                if(pos != RecyclerView.NO_POSITION){
                                    recyclerViewInterface.onUserClick(pos);
                                }
                            }
                        }
                    }
            );

        }
    }
}
