package app.me.nightfall.lobby;

public class ChatPostModel {

    public String message, senderID, username;
    public Object timestamp;

    public ChatPostModel() {
    }



    public ChatPostModel(String message, String senderID, Object timestamp, String username) {
        this.message = message;
        this.senderID = senderID;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }



}



