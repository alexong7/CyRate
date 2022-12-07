package com.example.cy_rate.Comments;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cy_rate.Review.Review;
import com.example.cy_rate.BusinessPosts.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findById(int id);
    void deleteById(int id);

    List<Comment> findByReview(Review review);
    List<Comment> findByPost(Post post);
}