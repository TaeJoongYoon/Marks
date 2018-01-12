package com.yoon.memoria.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yoon on 2017-12-20.
 */

public class Post {

    String uid;
    String postUid;
    double latitude;
    double longitude;
    String imgUri;
    String filename;
    String content;
    int likeCount = 0;
    int commentCount = 0;
    Map<String ,Boolean> likes = new HashMap<String, Boolean>();

    public Post() {}

    public Post(String uid, String postUid, double latitude, double longitude, String imgUri, String filename, String content){
        this.uid = uid;
        this.postUid = postUid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgUri = imgUri;
        this.filename  = filename;
        this.content = content;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("postUid",postUid);
        result.put("latitude",latitude);
        result.put("longitude",longitude);
        result.put("imgUri",imgUri);
        result.put("filename",filename);
        result.put("content",content);
        result.put("likeCount",likeCount);
        result.put("commentCount",commentCount);
        result.put("likes",likes);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
