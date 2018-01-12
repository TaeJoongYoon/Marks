package com.yoon.memoria.Model;

/**
 * Created by Yoon on 2018-01-12.
 */

public class Comment {
    private String Uid;
    private String commentUid;
    private String content;

    public Comment(String Uid,String commentUid, String content){
        this.Uid = Uid;
        this.commentUid = commentUid;
        this.content = content;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentUid() {
        return commentUid;
    }

    public void setCommentUid(String commentUid) {
        this.commentUid = commentUid;
    }
}
