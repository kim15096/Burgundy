package app.me.nightstory.lobby;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import app.me.nightstory.R;
import app.me.nightstory.TimeCalc;

public class StoryViewHolder extends RecyclerView.ViewHolder{

    private final View mView;
    public CardView cardView;

    public StoryViewHolder(@NonNull View itemView) {

        super(itemView);
        mView = itemView;
    }

    public void setMessage(String text){
        TextView title_tv = mView.findViewById(R.id.chat_message);
        title_tv.setText(text);

    }

    public void setProfilePicture(String text){
        CircularImageView pp = mView.findViewById(R.id.chat_pp);
        Glide.with(mView).load(text).centerCrop()
                .into(pp);
    }

    public void setTime(Long time){
        TextView commentTime = mView.findViewById(R.id.lobby_timestamp);
        String timeAgo = TimeCalc.getTimeAgo(time);
        commentTime.setText(timeAgo);
    }

    public void setUsername(String text){
        TextView userTV = mView.findViewById(R.id.chat_username);
        userTV.setText(text);
    }


}
