package app.me.nightstory.lobby;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import app.me.nightstory.R;

public class RoomViewHolder extends RecyclerView.ViewHolder{

    private final View mView;
    public CardView cardView;

    public RoomViewHolder(@NonNull View itemView) {

        super(itemView);
        mView = itemView;
        cardView = mView.findViewById(R.id.lobbyCardView);

    }

    public void setTitle(String text){
        TextView title_tv = mView.findViewById(R.id.lobbyL_title);
        text = "제 직업은 " + text + "입니다. 뭐든지 물어봐요!";
        title_tv.setText(text);

    }

    public void setCategory(String text){

    }

    public void setTot_Views(Long num){
        TextView cur_view = mView.findViewById(R.id.card_cur_views_tv);
        cur_view.setText(num+"");
    }

    public void setPicture(String text){
        ImageView pp = mView.findViewById(R.id.post_pp);
            Glide.with(itemView).load(text).centerCrop().into(pp);

    }

    public void setUsername(String text){
        TextView username = mView.findViewById(R.id.lobbyL_username);
        username.setText(text);
    }

}
