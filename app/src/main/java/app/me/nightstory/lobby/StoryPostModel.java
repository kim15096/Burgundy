package app.me.nightstory.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class StoryPostModel {

    public String text, ppURL, chatUsername, userID;
    public Date timestamp;

    public StoryPostModel() {
    }



    public StoryPostModel(String text, Timestamp timestamp, String ppURL, String chatUsername, String userID) {
        this.text = text;
        this.ppURL = ppURL;
        this.userID = userID;
        this.chatUsername = chatUsername;
        this.timestamp = timestamp.toDate();
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPpURL() {
        return ppURL;
    }

    public String getChatUsername() {
        return chatUsername;
    }
}



