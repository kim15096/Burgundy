package app.me.nightfall.lobby;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyPostModel {

    public String lobbyID, title, category, hostID;
    public Object timestamp;

    public LobbyPostModel() {
    }



    public LobbyPostModel(String category, String hostID, Object timestamp, String lobbyID, String title) {
        this.hostID = hostID;
        this.category = category;
        this.timestamp = timestamp;
        this.title = title;
        this.lobbyID = lobbyID;
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

}



