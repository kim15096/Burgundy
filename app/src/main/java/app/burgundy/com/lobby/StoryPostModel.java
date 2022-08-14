package app.burgundy.com.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class StoryPostModel {

    public String text, ppURL, chatUsername, userID, response, commentID;
    public Date timestamp;

    public StoryPostModel() {
    }



    public StoryPostModel(String text, String commentID, String response, Timestamp timestamp, String ppURL, String chatUsername, String userID) {
        this.text = text;
        this.ppURL = ppURL;
        this.response = response;
        this.userID = userID;
        this.chatUsername = chatUsername;
        this.timestamp = timestamp.toDate();
        this.commentID = commentID;
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

    public String getResponse() {
        return response;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setResponse(String response) {
        this.response = response;
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



