package com.yoon.memoria.Model;

import java.util.List;

/**
 * Created by Yoon on 2017-12-20.
 */

public class Post {

    private String filename;
    private String content;
    private double latitude;
    private double longitude;
    private String username;
    private String date;
    private List<Like> likes;

    public Post() {}

    public Post(String filename, String content, double latitude, double longitude, String username, String date, List<Like> likes){
        this.filename = filename;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        this.date = date;
        this.likes = likes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String nickname) {
        this.username = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
