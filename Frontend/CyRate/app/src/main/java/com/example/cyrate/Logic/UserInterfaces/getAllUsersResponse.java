package com.example.cyrate.Logic.UserInterfaces;

import com.example.cyrate.models.BusinessListCardModel;
import com.example.cyrate.models.UserListCardModel;
import com.example.cyrate.models.UserModel;

import java.util.List;

public interface getAllUsersResponse {
    public void onSuccess(List<UserListCardModel> list);
    public void onError(String s);

}
