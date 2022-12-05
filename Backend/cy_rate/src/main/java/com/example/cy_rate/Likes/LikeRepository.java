package com.example.cy_rate.Likes;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cy_rate.BusinessPosts.Post;
import com.example.cy_rate.Review.Review;

public interface LikeRepository extends JpaRepository<Like, Integer>{
    Like findById(int id);
    void deleteById(int id);

}