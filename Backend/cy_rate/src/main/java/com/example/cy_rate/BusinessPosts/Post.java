package com.example.cy_rate.BusinessPosts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.cy_rate.Business.Business;

import io.swagger.v3.oas.annotations.Hidden;


@Entity
@Table(name = "BusinessPosts")
public class Post {
    // pk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private int pid;

    @Column(name = "postTxt")
    private String postTxt;

    @Column(name = "date")
    private String date;

    @Column(name = "photoUrl")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "busId")
    private Business business;

public Post(){
    this.postTxt = "";
    this.date = "";
    this.photoUrl = "";

}


public Post(String postTxt, String date, String photo)
{
    this.postTxt = postTxt;
    this.date = date;
    this.photoUrl = photo;
    // this.likes = likes;
    // this.dislikes = dislikes;
}

public int getPid()
{
    return pid;
}

public void setPid(int pid)
{
    this.pid = pid;
}

public String getDate()
{
    return date;
}

public void setDate(String date)
{
    this.date = date;
}

public Business getBusiness()
{
    return business;
}

public void setBusiness(Business business)
{
    this.business = business;
}

public String getPostTxt()
{
    return postTxt;
}

public void setPostTxt(String postTxt)
{
    this.postTxt = postTxt;
}

public String getPhotoUrl()
{
    return photoUrl;
}

public void setPhotoUrl(String photo)
{
    this.photoUrl = photo;
}

@Override
public String toString()
{
    return postTxt + "\n" + date + "\n" + photoUrl + "\n";
}
}



