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
    private User user;

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

    // Store all socket session and their corresponding username.
  private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
  private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

  private final Logger logger = LoggerFactory.getLogger(LikeSocket.class);

  
  @OnOpen
  public void OnOpen(Session session, @PathParam("id") int id, @PathParam("uid") int uid) throws IOException
  {
    logger.info("Entered into Open");
    user = userRepo.findById(uid);
    sessionUsernameMap.put(session,user.getUsername());
    usernameSessionMap.put(user.getUsername(), session);
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    logger.info("Entered into close");

    String username = sessionUsernameMap.get(session);
    sessionUsernameMap.remove(session);
    usernameSessionMap.remove(username);

    String message = username + "disconnected";
  }


}
