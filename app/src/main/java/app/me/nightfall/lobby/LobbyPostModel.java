package app.me.nightfall.lobby;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyPostModel {

    public String lobbyID, title, category, hostID, p1_ID, p2_ID, p3_ID;
    public Object timestamp;
    public Long count;

    public LobbyPostModel() {
    }

    public LobbyPostModel(String category, String hostID, Object timestamp, String lobbyID, String title, String p1_ID, String p2_ID, String p3_ID, Long count) {
        this.hostID = hostID;
        this.category = category;
        this.timestamp = timestamp;
        this.title = title;
        this.lobbyID = lobbyID;
        this.p1_ID = p1_ID;
        this.p2_ID = p2_ID;
        this.p3_ID = p3_ID;
        this.count = count;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public String getUserID() {
        return hostID;
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

    public String getHostID() {return hostID;}

    public String getP1_ID() {
        return p1_ID;
    }

    public String getP2_ID() {
        return p2_ID;
    }

    public String getP3_ID() {
        return p3_ID;
    }

    public Long getCount() {
        return count;
    }



}



