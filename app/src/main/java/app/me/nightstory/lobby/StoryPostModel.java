package app.me.nightstory.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class StoryPostModel {

    public String text;
    public Date timestamp;

    public StoryPostModel() {
    }



    public StoryPostModel(String text, Timestamp timestamp) {
        this.text = text;
        this.timestamp = timestamp.toDate();
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }


}



