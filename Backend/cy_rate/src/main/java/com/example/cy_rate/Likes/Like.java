package com.example.cy_rate.Likes;
import com.example.cy_rate.Business.Business;
import com.example.cy_rate.BusinessPosts.Post;
import com.example.cy_rate.Review.Review;
import com.example.cy_rate.User.User;
import io.swagger.v3.oas.annotations.Hidden;

// JPA stuff
import javax.persistence.Entity;
import javax.persistence.Table;



import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;


@Entity
@Table(name = "Likes")
public class Like {

@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
@Hidden
@Column(name = "lid")
private int lid;

@Column
private int likeCount;

@Column
private String likeType;

@Hidden
@ManyToOne
@JoinColumn(name = "rid", referencedColumnName = "rid")
private Review review;


// @Hidden
// @ManyToOne
// @JoinColumn(name = "uid", referencedColumnName = "userId")
// private User user;

@Hidden
@ManyToOne
@JoinColumn(name = "pid", referencedColumnName = "pid")
private Post post;

public Like() {
    
}

public Like(int likeCount)
{
    this.likeCount = likeCount;
}

public int getLid()
{
    return lid;
}

public void setLid(int lid)
{
    this.lid = lid;
}

// public User getUser()
// {
//     return user;
// }

// public void setUser(User user)
// {
//     this.user = user;
// }

public Review getReview()
{
    return review;
}

public void setReview(Review review)
{
    this.review = review;
}

public Post getPost()
{
    return post;
}

public void setPost(Post post)
{
    this.post = post;
}

public String getLikeType()
{
    return likeType;
}

public void setLikeType(String likeType)
{
    this.likeType = likeType;
}

public int getLikeCount()
{
    return likeCount;
}

public void setLikeCount(int likeCount)
{
    this.likeCount = likeCount;
}




}
