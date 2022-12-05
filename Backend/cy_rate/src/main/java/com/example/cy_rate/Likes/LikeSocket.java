package com.example.cy_rate.Likes;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.cy_rate.Review.ReviewRepository;
import com.example.cy_rate.User.User;
import com.example.cy_rate.User.UserRepository;
import com.example.cy_rate.BusinessPosts.Post;
import com.example.cy_rate.BusinessPosts.PostRepository;
import com.example.cy_rate.Review.Review;

@Controller
@ServerEndpoint(value = "/likes/{id}/{uid}")
public class LikeSocket {
    private static ReviewRepository reviewRepo;
    private static PostRepository postRepo;
    private static UserRepository userRepo;
    private static LikeRepository likeRepo;

    @Autowired
    public void setReviewRepository(ReviewRepository repo)
    {
        reviewRepo = repo;
    }

    public void setLikeRepository(LikeRepository repo)
    {
        likeRepo = repo;
    }

    public void setUserRepository(UserRepository repo)
    {
        userRepo = repo;
    }

    public void setPostRepository(PostRepository repo)
    {
        postRepo = repo;
    }
}
