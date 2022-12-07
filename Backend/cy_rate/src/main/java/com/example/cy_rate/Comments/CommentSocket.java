package com.example.cy_rate.Comments;

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

import com.google.gson.Gson;


/**
 * Sample Tester for postman
 * ws://localhost:8080/comments/businessPost/5/7
 * {"commenterName": "Anbu Krishnan", "commentType": "businessPost", "photoUrl": "google.images.com", "commentBody": "comment 8:15", "date":"12/2/2022"}
 */

 /**
  * Websocket controller for Comment type, Handles connection, close, messages, & errors
  * Also stores recieved messages using a CommentRepository
  */
@Controller
@ServerEndpoint(value = "/comments/{type}/{id}/{uid}") //"/comments/review/rid of review/uid that is connecting/commenting"
public class CommentSocket {                           //"/comments/businessPost/post id/uid that is connecting/commenting"
    
    private static UserRepository userRepo;

    private static ReviewRepository reviewRepo;

    private static PostRepository postRepo;

    private static CommentRepository commentRepo;

    private User usr; //global user that is connected to websocket

    // auto wire all repositories used
    @Autowired
    public void setUserRepository(UserRepository repo)
    {
        userRepo = repo;
    }

    @Autowired
    public void setReviewRepository(ReviewRepository repo)
    {
        reviewRepo = repo;
    }

    @Autowired
    public void setCommentRepository(CommentRepository repo)
    {
        commentRepo = repo;
    }

    @Autowired
    public void setPostRepository(PostRepository repo)
    {
        postRepo = repo;
    }

    // Maps for storing connected users by their sesison and username
    private static Map< Session, String > sessionUsernameMap = new Hashtable<>();
    private static Map< String, Session > usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(CommentSocket.class);

    /**
     * Handles connection to websocket, Adds user and session to maps
     * Verifies post/review exists, removes user and session from maps and disconnects if doesn't
     * 
     * Then calls sendMessageToParticularUser to return them the chat history pertaining the 
     * post they are connected to
     * 
     * @param session Websocket session
     * @param id Post/Review id
     * @param uid User id that is connected to websocket
     * @param type "review"/"businessPost" type of comment
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") int id, @PathParam("uid") int uid, @PathParam("type") String type)
    throws IOException {
        logger.info("Entered into open");
        usr = userRepo.findById(uid);
        sessionUsernameMap.put(session, usr.getUsername());
        usernameSessionMap.put(usr.getUsername(), session);
        
        // see if post/review exists
        // if doesn't exist close the session
        // remove session from maps so we don't broadcast to a disconnected user
        if(!checkPostExists(type, id)){
            session.close();
            sessionUsernameMap.remove(session);
            usernameSessionMap.remove(usr.getUsername());
        }
        // return chat history to newly connected user
        sendMessageToParticularUser(usr.getUsername(), getAllComments(type, id));
    }

    /**
     * Handles incoming message from websocket
     * 
     * Parses message using Gson
     * Creates Comment and sets the review/post it is connected to
     * Saves to commentRepo then broadcast() to all other connected users
     * 
     * @param session
     * @param message
     * @param id
     * @throws IOException
     */
    @OnMessage
	public void onMessage(Session session, String message, @PathParam("id") int id) throws IOException {
		
        logger.info("Entered into Message: Got Message:" + message);
        
        // message is the request body sent to the websocket
        // parse the string into a json object
        // check type and set review/busPost accordingly
        Gson gson = new Gson();
        Comment comment = gson.fromJson(message, Comment.class);
        if(comment.getCommentType().equals("review"))
        {
            Review review = reviewRepo.findById(id);
            comment.setReview(review);
        }
        else if(comment.getCommentType().equals("businessPost"))
        {
            Post post = postRepo.findById(id);
            comment.setPost(post);
        }
        
		// Saving chat history to repository
        comment.setUser(usr); //set user as well
        commentRepo.save(comment);
        broadcast(comment);
	}

    /**
     * Handles closing connection
     * Removes user and session from global maps
     * broadcast() discconect to other users
     * 
     * @param session websocket session
     * @throws IOException
     */
	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

    // remove the user connection information
		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

    // broadcase that the user disconnected
		String message = username + " disconnected";
		broadcast(message);
	}

    /**
     * Handles error in websocket
     * @param session
     * @param throwable
     */
    @OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

    /**
     * Parses Comment Object using gson and sends json object to each connected client
     * Excluding the one that sent it
     * 
     * @param comment Comment object to be parsed
     */
    private void broadcast(Comment comment)
    {

        sessionUsernameMap.forEach((session, username) -> {
            try {
                Gson gson = new Gson();
                // if its the user that sent the comment don't send message
                // to them
                if(!usr.getUsername().equals(username)){
                    usernameSessionMap.get(username).getBasicRemote().sendText(gson.toJson(comment));
                }
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }

    /**
     * Sends String message to all connected users, excluding user of this instance
     * @param text msg to be sent
     */
    private void broadcast(String text)
    {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                if(!usr.getUsername().equals(username)){
                    usernameSessionMap.get(username).getBasicRemote().sendText(text);
                }
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }

    /**
     * Iterates through list of comments and sends each as a parsed json object using Gson
     * to a particular user given by username
     * 
     * @param username user msg is to be sent to
     * @param comments list of comment objects
     */
    private void sendMessageToParticularUser(String username, List<Comment> comments) {
		try {
            Gson gson = new Gson();
            for(Comment comment : comments){
                usernameSessionMap.get(username).getBasicRemote().sendText(gson.toJson(comment));
            }
		} 
        catch (IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

    /**
     * Gets all comments pertaining to a certain post/review
     * 
     * @param type "businessPost"/"review"
     * @param id id of post/review
     * @return comments from post
     */
    private List<Comment> getAllComments(String type, int id)
    {
        if(type.equals("review"))
        {
            Review review = reviewRepo.findById(id);
            return commentRepo.findByReview(review);
        }
        else if(type.equals("businessPost"))
        {
            Post post = postRepo.findById(id);
            return commentRepo.findByPost(post);
        }
        else
        {
            return new ArrayList<Comment>();
        }
    }

    
    /**
     * Checks repos for post/review given by type and id
     * 
     * @param type "review"/"businessPost"
     * @param id id of post/review
     * @return True if exists, false if not exists
     */
    private boolean checkPostExists(String type, int id)
    {
        try{
            if(type.equals("review"))
            {
                Review review = reviewRepo.findById(id);
                if(review == null)
                {
                    usernameSessionMap.get(usr.getUsername()).getBasicRemote().sendText("Reiew with ID: " + id + " not found");
                    return false;
                }
            }
            else if(type.equals("businessPost"))
            {
                Post post = postRepo.findById(id);
                if(post == null)
                {
                    usernameSessionMap.get(usr.getUsername()).getBasicRemote().sendText("Business Post with ID: " + id + " not found");
                    return false;
                }
            }
            //post exists
            return true;
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
            return false;
        }
    }
}
