package app.burgundy.com.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class CommentsPostModel {

    public String comment, username;
    public Date timestamp;

    public CommentsPostModel() {
    }



    public CommentsPostModel(String comment, String username, Timestamp timestamp) {
        this.comment = comment;
        this.username = username;
        this.timestamp = timestamp.toDate();
    }

    public String getComment() {
        return comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }
}



