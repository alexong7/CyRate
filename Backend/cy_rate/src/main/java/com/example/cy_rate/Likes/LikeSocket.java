package com.example.cy_rate.Likes;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

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

import com.example.cy_rate.BusinessPosts.Post;
import com.example.cy_rate.BusinessPosts.PostRepository;
import com.example.cy_rate.Review.Review;
import com.example.cy_rate.Review.ReviewRepository;
import com.example.cy_rate.User.User;
import com.example.cy_rate.User.UserRepository;

import com.google.gson.Gson;


//"/likes/review/rid/uid", 
//"/likes/businessPost/bid/uid"

@Controller
@ServerEndpoint(value = "/likes/{type}/{id}/{uid}") 
public class LikeSocket {
    private static ReviewRepository reviewRepo;
    private static PostRepository postRepo;
    private static UserRepository userRepo;
    private static LikeRepository likeRepo;
    private User user;
    private int likecount;

    @Autowired
    public void setReviewRepository(ReviewRepository repo)
    {
        reviewRepo = repo;
    }
    @Autowired
    public void setLikeRepository(LikeRepository repo)
    {
        likeRepo = repo;
    }

    @Autowired
    public void setUserRepository(UserRepository repo)
    {
        userRepo = repo;
    }

    @Autowired
    public void setPostRepository(PostRepository repo)
    {
        postRepo = repo;
    }

    //Store all socket session and their corresponding username.
  private static Map <Session, String> sessionUsernameMap = new Hashtable < > ();
  private static Map <String, Session> usernameSessionMap = new Hashtable < > ();

  private final Logger logger = LoggerFactory.getLogger(LikeSocket.class);

  
  /**
   * 
   */
  @OnOpen
  public void OnOpen(Session session, @PathParam("id") int id, @PathParam("uid") int uid, @PathParam("type") String type) throws IOException
  {
    logger.info("Entered into Open");
    user = userRepo.findById(uid);
    sessionUsernameMap.put(session,user.getUsername());
    usernameSessionMap.put(user.getUsername(), session);
    sendLikeObject(user.getUsername(), getAllLikes(type,id));
  }


  /**
   * 
   */
  @OnClose
  public void onClose(Session session) throws IOException {
    logger.info("Entered into close");

    String username = sessionUsernameMap.get(session);
    sessionUsernameMap.remove(session);
    usernameSessionMap.remove(username);

    String message = username + "disconnected";
    // broadcast(message);
  }


  /**
   * 
   */
  @OnMessage 
  public void onMessage(Session session, String message, @PathParam("id") int id) throws IOException
  { 
    
    Gson gson = new Gson();
    Like like = gson.fromJson(message, Like.class);
    if(like.getLikeType().equals("Review"))
    {
        Review review = reviewRepo.findById(id);
        like.setReview(review);
    } 
    else if(like.getLikeType().equals("businessPost"))
    {
        Post post = postRepo.findById(id);
        like.setPost(post);

    }

    like.setUser(user);
    likeRepo.save(like);
    broadcast(like);

  }


  /**
   * 
   */
  @OnError
  public void OnError(Session session, Throwable throwable)
  {
    logger.info("Entered into Error");
  }



  /**
   * 
   */
  private Like getAllLikes(String type, int id)
  {
    if(type.equals("review"))
    {
        Review review = reviewRepo.findById(id);
        return likeRepo.findByReview(review);
    }
    else if(type.equals("businessPost"))
    {
        Post post = postRepo.findById(id);
        return likeRepo.findByPost(post);
    }
    else
    {
        return new Like();
    }
  }


  /**
   * 
   */
  private void sendLikeObject(String username, Like like) {

    try {
        Gson gson = new Gson();
        usernameSessionMap.get(username).getBasicRemote().sendText(gson.toJson(like));
    }
    catch(IOException e) {
        e.printStackTrace();
    }
}


    /**
     * 
     */
  private void broadcast (Like like)
  {
    sessionUsernameMap.forEach((session, username) -> {
        try {
            Gson gson = new Gson();

            if(!user.getUsername().equals(username)){
                usernameSessionMap.get(username).getBasicRemote().sendText(gson.toJson(like));
            }
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    });



    }
}
  


