package app.me.nightfall.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class ChatPostModel {

    public String message, senderID, username;
    public Date timestamp;

    public ChatPostModel() {
    }



    public ChatPostModel(String message, String senderID, Timestamp timestamp, String username) {
        this.message = message;
        this.senderID = senderID;
        this.timestamp = timestamp.toDate();
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }



}



