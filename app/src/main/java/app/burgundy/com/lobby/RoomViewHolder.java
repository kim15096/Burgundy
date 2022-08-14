package app.burgundy.com.lobby;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import app.burgundy.com.R;
import app.burgundy.com.TimeCalc;

public class RoomViewHolder extends RecyclerView.ViewHolder{

    private final View mView;
    public CardView cardView, imgCV;

    public RoomViewHolder(@NonNull View itemView) {

        super(itemView);
        mView = itemView;
        cardView = mView.findViewById(R.id.lobbyCardView);
        imgCV = mView.findViewById(R.id.post_imgCV);

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

    public void setTime(Long time){
        TextView card_time = mView.findViewById(R.id.card_Time);
        String timeAgo = TimeCalc.getTimeAgo(time);
        card_time.setText(timeAgo);
    }


    public void setPicture(String text){
        ImageView pp = mView.findViewById(R.id.post_pp);
            Glide.with(itemView).load(text).apply(new RequestOptions().override(175, 175)).centerCrop().into(pp);

    }

    public void setUsername(String text){
        TextView username = mView.findViewById(R.id.lobbyL_username);
        username.setText(text);
    }

}
