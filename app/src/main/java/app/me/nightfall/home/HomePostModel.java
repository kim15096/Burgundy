package app.me.nightfall.home;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePostModel {

    public String description, userID, post_id, title, category, keyword;
    public Object timestamp;

    public HomePostModel() {
    }



    public HomePostModel(String description, String category, String userID, Object timestamp, String post_id, String title, String keyword) {
        this.description = description;
        this.userID = userID;
        this.category = category;
        this.timestamp = timestamp;
        this.title = title;
        this.post_id = post_id;
        this.keyword = keyword;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getUser_id() {
        return userID;
    }

    public void setUser_id(String user_id) {
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

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}



