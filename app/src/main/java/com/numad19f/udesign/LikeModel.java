package com.numad19f.udesign;

import java.time.LocalDateTime;

public class LikeModel {
    private int id;
    private int imgId;
    private String username;
    private String likedOn;

    public LikeModel(int id, int imgId, String username) {
        this.id = id;
        this.imgId = imgId;
        this.username = username;
    }

    public LikeModel(int id, int imgId, String username, String likedOn) {
        this.id = id;
        this.imgId = imgId;
        this.username = username;
        this.likedOn = likedOn;
    }

    public int getId() {
        return id;
    }

    public int getImageId() {
        return imgId;
    }

    public String getUserName() {
        return username;
    }

    public String getLikedOn() {
        return likedOn;
    }

    public void setLikedOn(String likedOn) {
        this.likedOn = likedOn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
