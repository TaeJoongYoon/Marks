package com.yoon.memoria.Comment;

/**
 * Created by Yoon on 2018-01-12.
 */

public class CommentContract {
    public interface View{
        void toUser(String Uid);
        void delete(String Uid, String commentUid);
    }

    interface Adapter{

    }
}
