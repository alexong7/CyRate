package com.example.cy_rate.BusinessPosts;

import javax.persistence.*;

import com.example.cy_rate.Business.Business;

import io.swagger.v3.oas.annotations.Hidden;

import java.sql.Blob;


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

    @Lob
    @Column(name = "blobPhoto", columnDefinition="BLOB")
    private byte[] blobPhoto;


    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "busId")
    private Business business;

public Post(){
    this.postTxt = "";
    this.date = "";
//    this.photoUrl = "";
}

public Post(String postTxt, String date)
{
    this.postTxt = postTxt;
    this.date = date;
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


public byte[] getBlobPhoto() { return blobPhoto; }

public void setBlobPhoto(byte[] blobPhoto) { this.blobPhoto = blobPhoto; }

}



