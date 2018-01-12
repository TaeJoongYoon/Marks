package com.yoon.memoria.Model;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoon on 2017-12-20.
 */

public class User {

    private String Uid;
    String nickname;
    String imgUri;
    String filename;
    String profile;
    int followingCount = 0;
    Map<String, Boolean> following = new HashMap<>();
    int followerCount = 0;
    Map<String, Boolean> follower = new HashMap<>();
    int quizCount = 0;
    int ansCount = 0;

    public User(){}

    public User(String Uid, String nickname,String imgUri, String profile){
        this.Uid = Uid;
        this.nickname = nickname;
        this.imgUri = imgUri;
        this.profile = profile;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Uid",Uid);
        result.put("nickname", nickname);
        result.put("imgUri",imgUri);
        result.put("filename",filename);
        result.put("followingCount", followingCount);
        result.put("following",following);
        result.put("followerCount",followerCount);
        result.put("follower",follower);
        result.put("quizCount",quizCount);
        result.put("ansCount",ansCount);

        return result;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public Map<String, Boolean> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, Boolean> following) {
        this.following = following;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public Map<String, Boolean> getFollower() {
        return follower;
    }

    public void setFollower(Map<String, Boolean> follower) {
        this.follower = follower;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getQuizCount() {
        return quizCount;
    }

    public void setQuizCount(int quizCount) {
        this.quizCount = quizCount;
    }

    public int getAnsCount() {
        return ansCount;
    }

    public void setAnsCount(int ansCount) {
        this.ansCount = ansCount;
    }
}
