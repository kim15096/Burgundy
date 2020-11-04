package app.me.nightfall.lobby;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyPostModel {

    public String lobbyID, title, category, hostID, p1_ID, p2_ID, p3_ID, lang;
    public Object timestamp;
    public Long tot_views, cur_views;

    public LobbyPostModel() {
    }

    public LobbyPostModel(String category, String hostID, Object timestamp, String lobbyID, String title, String p1_ID, String p2_ID, String p3_ID, Long tot_views, String lang) {
        this.hostID = hostID;
        this.category = category;
        this.timestamp = timestamp;
        this.title = title;
        this.lang = lang;
        this.lobbyID = lobbyID;
        this.p1_ID = p1_ID;
        this.p2_ID = p2_ID;
        this.p3_ID = p3_ID;
        this.tot_views = tot_views;
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

    public Long getTot_views() {
        return tot_views;
    }

    public String getLang(){
        return lang;
    }



}



