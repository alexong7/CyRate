package com.example.cy_rate.Review;
import java.util.List;

import com.example.cy_rate.Business.Business;
import com.example.cy_rate.User.User;
import com.example.cy_rate.Business.BusinessRepository;
import com.example.cy_rate.User.UserRepository;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


/**
 * Sample Review Object
 * {
 * "business": 1,
 * "user": 7,
 * "rateVal": 4,
 * "reviewTxt": "good food, bad service"
 * }
 */
@RestController
public class ReviewController {
    @Autowired
    BusinessRepository businessRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ReviewRepository reviewRepo;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     *
     * @return all reviews in database table
     */
    @GetMapping(path="/reviews/all")
    List<Review> getAllReviews()
    {
        return reviewRepo.findAll();
    }

    /**
     * 
     * @param bid specific business id
     * @return all reviews for a specific business
     */
    @Operation(summary = "Get all reviews for a specific business")
    @GetMapping(path="/reviews/business/{bid}")
    List<Review> getReviewsByBusiness(@PathVariable int bid)
    {
        Business b = businessRepo.findById(bid);
        return reviewRepo.findByBusiness(b);
    }
    
    /**
     *  
     * @param uid the user id
     * @return all reviews for a specific user
     */
    @Operation(summary = "Get all reviews for a specific user")
    @GetMapping(path="/reviews/user/{uid}")
    List<Review> getReviewsByUser(@PathVariable int uid)
    {
        User u = userRepo.findById(uid);
        return reviewRepo.findByUser(u);
    }

    /**
     * 
     * @param bid - business id for review
     * @param uid - user id that is leaving review
     * @param review - Review Object {"rateVal": int, "reviewTxt": Str}
     * @return suc/fail string
     */
    @Operation(summary = "Create a review that connects to a business and a user")
    @PostMapping(path="/review/{bid}/user/{uid}/createReview")
    String createReview(@PathVariable int bid, @PathVariable int uid, @RequestBody Review review)
    {
        try {
            // Get user and business related to review
            Business b = businessRepo.findById(bid);
            User u = userRepo.findById(uid);
    
            // Set reviews business and user to ones found above
            review.setBusiness(b);
            review.setUser(u);

            // update business review count and sum
            b.setReviewCount(b.getReviewCount() + 1);
            b.setReviewSum(b.getReviewSum() + review.getRateVal());
            // Save all changes
            reviewRepo.save(review);
            return success;
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Update a specific review, only able to update reviewHeader, raateVal, and reviewTxt
     * Cannot change what user left the review or what business it is for 
     * @param rid review id
     * @return success/failure str
     */
    @Operation(summary = "Update reviewHeader, rateVal, and reviewTxt for a specific review")
    @PutMapping(path="/reveiw/update/{rid}")
    String updateReview(@PathVariable int rid, @RequestBody Review newR)
    {
        Review r = reviewRepo.findById(rid);
        r.setReviewHeader(newR.getReviewHeader());

        // Update Business review sum to reflect updated review
        if(r.getRateVal() != newR.getRateVal())
        {
            Business temp = businessRepo.findById(r.getBusiness().getBusId());
            temp.setReviewSum(temp.getReviewSum() - r.getRateVal());
            temp.setReviewSum(temp.getReviewSum() + newR.getRateVal());
            r.setRateVal(newR.getRateVal());
        }
        r.setReviewTxt(newR.getReviewTxt());
        reviewRepo.save(r);
        return success;
    }

    /***
     * Delete Review
     * @param rid review id to delete
     * @return success/failure str
     */
    @Operation(summary = "Delete a review from DB")
    @DeleteMapping(path="/review/delete/{rid}")
    String deleteReview(@PathVariable int rid)
    {
        // update business review count and sum
        Review r = reviewRepo.findById(rid);
        Business b = r.getBusiness();
        b.setReviewCount(b.getReviewCount() - 1);
        b.setReviewSum(b.getReviewSum() - r.getRateVal());
        businessRepo.save(b);

        // delete review
        reviewRepo.deleteById(rid);
        return success;
    }
       
}
