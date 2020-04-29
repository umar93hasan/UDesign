package com.numad19f.udesign;

public class ImageModel implements Comparable< ImageModel >{
    private int id;
    private String image;
    private String imagetype;
    private int likecount;
    private String username;

    public ImageModel(int id, String image, String imagetype, int likecount, String username) {
        this.id = id;
        this.image = image;
        this.imagetype = imagetype;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getImagetype() {
        return imagetype;
    }

    public int getLikecount() {
        return likecount;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(ImageModel obj) {
        return Integer.compare(obj.getLikecount(), this.getLikecount());
    }
}
