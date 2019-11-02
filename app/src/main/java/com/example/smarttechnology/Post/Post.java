package com.example.smarttechnology.Post;

public class Post {


    public String postId,description,postimage,pTime,uid, uName, uEmail, profileImage;


    public Post() {
    }

    public Post(String postId, String description, String postimage, String pTime, String uid, String uName, String uEmail, String profileImage) {
        this.postId = postId;
        this.description = description;
        this.postimage = postimage;
        this.pTime = pTime;
        this.uid = uid;
        this.uName = uName;
        this.uEmail = uEmail;
        this.profileImage = profileImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
