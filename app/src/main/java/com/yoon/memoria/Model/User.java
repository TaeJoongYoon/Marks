package com.yoon.memoria.Model;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoon on 2017-12-20.
 */

public class User {

    private String nickname;
    private String password;
    private int followingCount = 0;
    private Map<String, Boolean> following = new HashMap<>();
    private int followerCount = 0;
    private Map<String, Boolean> follower = new HashMap<>();

    public User(){}

    public User(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nickname", nickname);
        result.put("password", password);
        result.put("followingCount", followingCount);
        result.put("following",following);
        result.put("followerCount",followerCount);
        result.put("follower",follower);

        return result;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
