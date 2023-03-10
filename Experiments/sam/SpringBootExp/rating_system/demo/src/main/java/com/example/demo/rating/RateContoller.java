package com.example.demo.rating;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
//import java.util.ArrayList;


@RestController
public class RateContoller {
    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL
    /**
     * Messing around with ArrayList
     * Hashmap better choice
     * 
    @GetMapping("/rating")
    public @ResponseBody ArrayList<Rating> getAllRatings() {
        return ratingList2;
    }
    */

    //hashmap used to store all ratings, can index by restName
    HashMap<String, Rating> ratingList = new  HashMap<>();

    // THIS IS THE LIST OPERATION
    @GetMapping("/rating")
    public @ResponseBody HashMap<String,Rating> getAllRatings() {
        return ratingList;
    }
      
    // THIS IS THE CREATE OPERATION
    // post a new rating
    @PostMapping("/rating")
    public @ResponseBody String createRating(@RequestBody Rating rating) {
        System.out.println(rating);
        ratingList.put(rating.getRestName(), rating);
        return "New rating for "+ rating.getRestName() + " Saved";
    }

    // THIS IS THE READ OPERATION
    // returns review for given resturant name
    @GetMapping("/rating/{restName}")
    public @ResponseBody Rating getRating(@PathVariable String restName) {
        Rating r  = ratingList.get(restName);
        return r;
    }

    // THIS IS THE UPDATE OPERATION
    // update a rating
    @PutMapping("/rating/{restName}")
    public @ResponseBody Rating updateRating(@PathVariable String restName, @RequestBody Rating r) {
        ratingList.replace(restName, r);
        return ratingList.get(restName);
    }

    // THIS IS THE DELETE OPERATION
    @DeleteMapping("/rating/{restName}")
    public @ResponseBody HashMap<String, Rating> deleteRating(@PathVariable String restName) {
        ratingList.remove(restName);
        return ratingList;
    }
}
