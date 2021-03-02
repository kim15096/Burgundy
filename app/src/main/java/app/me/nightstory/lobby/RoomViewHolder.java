package app.me.nightstory.lobby;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.me.nightstory.R;

public class RoomViewHolder extends RecyclerView.ViewHolder{

    private final View mView;
    public ImageView joinBtn;
    public CardView cardView;

    public RoomViewHolder(@NonNull View itemView) {

        super(itemView);
        mView = itemView;
        joinBtn = mView.findViewById(R.id.joinBtn);
        cardView = mView.findViewById(R.id.lobbyCardView);

    }

    public void setTitle(String text){
        TextView title_tv = mView.findViewById(R.id.username);
        title_tv.setText(text);

    }

    public void setCategory(String text){
        TextView category = mView.findViewById(R.id.post_category);
        category.setText(text);
    }

    public void setCur_views(Long num){
        TextView cur_view = mView.findViewById(R.id.card_cur_views_tv);
        cur_view.setText(num+"");
    }

}
