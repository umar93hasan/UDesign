package com.numad19f.udesign;

import java.io.Serializable;

public class ViewImageModel implements Serializable {
    private int id;
    private String image;
    private String imageType;
    private String userName;
    private int likeCount;
    private boolean userLiked;
    private int likeId;

    public ViewImageModel(int id, String image, String imageType, int likeCount, boolean userLiked, String userName, int likeId) {
        this.id = id;
        this.image = image;
        this.imageType = imageType;
        this.likeCount = likeCount;
        this.userLiked = userLiked;
        this.userName=userName;
        this.likeId=likeId;
    }

    ViewImageModel()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    @Override
    public String toString(){
        return id+", "+userLiked;
    }
}
