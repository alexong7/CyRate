package com.example.cy_rate.Comments;
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
@Table(name = "Comments")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    @Column(name="cid")
    private int cid;

    private String commenterName;

    @Column
    private String commentType;

    @Column
    private String photoUrl;

    @Column
    private String commentBody;

    @Column
    private String date;

    // @Hidden
    // @ManyToOne
    // @JoinColumn(name = "bid", referencedColumnName = "busId")
    // private Business business;

    @Hidden
    @ManyToOne
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    private Post post;

    @Hidden
    @ManyToOne
    @JoinColumn(name = "rid", referencedColumnName = "rid")
    private Review review;

    @Hidden
    @ManyToOne 
    @JoinColumn(name = "uid", referencedColumnName = "userId")
    private User user;



    public Comment() {
        this.photoUrl = "";
        this.commentBody = "";
        this.date = "";
        this.commenterName = "";
    }

    public Comment(String photoUrl, String commentBody, String date, String commentType)
    {
        this.photoUrl = photoUrl;
        this.commentBody = commentBody;
        this.date = date;
        this.commentType = commentType;
    }

    //------------ Getter Setters ------------------ //

    public int getCid()
    {
        return cid;
    }

    public void setCid(int cid)
    {
        this.cid = cid;
    }

    public String getphotoUrl()
    {
        return photoUrl;
    }

    public void setphotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public String getdate()
    {
        return date;
    }

    public void setdate(String date)
    {
        this.date = date;
    }

    // public Business getBusiness()
    // {
    //     return business;
    // }

    // public void setBusiness(Business bus)
    // {
    //     this.business = bus;
    // }
    
    public Post getPost()
    {
        return post;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }


    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getCommenterName()
    {
        return commenterName;
    }

    public void setCommenterName(String commenterName)
    {
        this.commenterName = commenterName;
    }

    public String getCommentType()
    {
        return commentType;
    }

    public void setCommentType(String commentType)
    {
        this.commentType = commentType;
    }

    public Review getReview()
    {
        return review;
    }

    public void setReview(Review review)
    {
        this.review = review;
    }


}
