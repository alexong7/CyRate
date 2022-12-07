package com.example.cy_rate.BusinessPosts;
import java.util.Arrays;
import java.util.List;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cy_rate.Business.BusinessRepository;

import io.swagger.v3.oas.annotations.Operation;

import com.example.cy_rate.Business.Business;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepo;

    @Autowired
    BusinessRepository businessRepo;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * 
     * @return all business posts 
     */
    @GetMapping(path = "/posts/all")
    List<Post> getAllPosts()
    {
        return postRepo.findAll();
    }

    /**
     * 
     * @param bid
     * @return get all the posts for a business by their ID
     */
    @GetMapping(path = "/posts/{bid}")
    List<Post> getPostsByBusiness(@PathVariable int bid)
    {
        Business b = businessRepo.findById(bid);
        return postRepo.findByBusiness(b);
    }

    /**
     * 
     * @param post , bid
     * @return create a business post
     */
    @PostMapping(path = "/posts/create/{bid}")
    String createPost(@RequestBody Post post, @PathVariable int bid)
    {
        
        try{
            Business b = businessRepo.findById(bid);
            post.setBusiness(b);

            postRepo.save(post);
            return success;
        }
        catch(Exception e)
        {
            return e.toString();
        }
        
    }

    /**
     * Deletes Post through the bid that its given
     * @param pid
     * @return if the deletion was succesful or a failure
     */
    @DeleteMapping(path = "/posts/delete/{pid}")
    String deletePost(@PathVariable int pid)
    {
        postRepo.deleteById(pid);
        return success;
    }

    /**
     * Updates a post by its Business Id.
     *  Allows you to edit a business post
     * @param pid
     * @param eP
     * @return
     */
    @PutMapping(path = "/posts/update/{pid}")
    String editPost(@PathVariable int pid, @RequestBody Post eP)
    {
        Post p = postRepo.findById(pid);
        p.setPostTxt(eP.getPostTxt());
//        p.setPhotoUrl(eP.getPhotoUrl());
        p.setBlobPhoto(eP.getBlobPhoto());
        p.setDate(eP.getDate());
        postRepo.save(p);
        return success;
    }
}