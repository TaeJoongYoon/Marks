package com.yoon.memoria.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yoon on 2017-12-20.
 */

public class Post {

    private String uid;
    private String nickname;
    private String date;
    private double latitude;
    private double longitude;
    private String filename;
    private String content;
    private int likeCount = 0;
    private Map<String ,Boolean> likes = new HashMap<String, Boolean>();

    public Post() {}

    public Post(String uid, String nickname, String date, double latitude, double longitude, String filename, String content){
        this.uid = uid;
        this.nickname = nickname;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.filename  = filename;
        this.content = content;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("nickname",nickname);
        result.put("date",date);
        result.put("latitude",latitude);
        result.put("longitude",longitude);
        result.put("filename",filename);
        result.put("content",content);
        result.put("likeCount",likeCount);
        result.put("likes",likes);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }
}
