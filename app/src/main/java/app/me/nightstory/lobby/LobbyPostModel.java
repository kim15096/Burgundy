package app.me.nightstory.lobby;

import com.google.firebase.Timestamp;

import java.util.Date;

public class LobbyPostModel {

    public String active, desc, title, lobbyID, hostName, hostID, imageURL;
    public Date timestamp;
    public Long tot_views;

    public LobbyPostModel() {
    }



    public LobbyPostModel(String active, String imageURL, String desc, String title, String lobbyID, String hostID, String hostName, Timestamp timestamp, Long tot_views) {
        this.desc = desc;
        this.timestamp = timestamp.toDate();
        this.title = title;
        this.imageURL = imageURL;
        this.lobbyID = lobbyID;
        this.active = active;
        this.hostID = hostID;
        this.hostName = hostName;
        this.tot_views = tot_views;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(String lobbyID) {
        this.lobbyID = lobbyID;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTot_views() {
        return tot_views;
    }

    public void setTot_views(Long tot_views) {
        this.tot_views = tot_views;
    }
}



