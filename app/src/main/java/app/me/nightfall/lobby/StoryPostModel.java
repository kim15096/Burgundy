package app.me.nightfall.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class StoryPostModel {

    public String text, senderID, username;
    public Date timestamp;
    public Long position;

    public StoryPostModel() {
    }



    public StoryPostModel(String text, Timestamp timestamp, Long position) {
        this.text = text;
        this.timestamp = timestamp.toDate();
    }

    public String getText() {
        return text;
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



