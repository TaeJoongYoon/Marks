package com.yoon.memoria.Model;

/**
 * Created by Yoon on 2017-12-21.
 */

public class Like {
    private String nickname;

    public Like(String nickname){
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
