package app.me.nightstory.article;

import com.google.firebase.Timestamp;

import java.util.Date;

public class ArticlePostModel {

    public String content, title, postID, poster, posterID;
    public Date timestamp;
    public Long likes;
    public Long tot_views;

    public ArticlePostModel() {
    }


    public ArticlePostModel(String content, String title, String postID, String posterID, String poster, Timestamp timestamp, Long likes, Long tot_views) {
        this.content = content;
        this.timestamp = timestamp.toDate();
        this.title = title;
        this.postID = postID;
        this.posterID = posterID;
        this.poster = poster;
        this.likes = likes;
        this.tot_views = tot_views;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getTot_views() {
        return tot_views;
    }

    public void setTot_views(Long tot_views) {
        this.tot_views = tot_views;
    }
}



