package app.me.nightfall.home;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyPostModel {

    public String desc, username, lobbyID, title, category, userID;
    public Object timestamp;
    public boolean featured;

    public LobbyPostModel() {
    }



    public LobbyPostModel(String description, String category, String userID, Object timestamp, String lobbyID, String title) {
        this.desc = description;
        this.userID = userID;
        this.category = category;
        this.timestamp = timestamp;
        this.title = title;
        this.lobbyID = lobbyID;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(String lobbyID) {
        this.lobbyID = lobbyID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;

    }

}



