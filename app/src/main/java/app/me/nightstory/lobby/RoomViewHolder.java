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
        TextView title_tv = mView.findViewById(R.id.username);
        String newText = "\"" + text + "\"";
        title_tv.setText(newText);

    }

    public void setCategory(String text){
        TextView category = mView.findViewById(R.id.post_category);
        category.setText(text);
    }

    public void setCur_views(Long num){
        TextView cur_view = mView.findViewById(R.id.card_cur_views_tv);
        cur_view.setText(num+"");
    }

    public void setProfilePicture(String text){
        CircularImageView pp = mView.findViewById(R.id.live_PP);
        if (!text.equals("")){
            Glide.with(itemView).load(text).centerCrop().into(pp);
        }
        else {
            pp.setImageResource(R.drawable.ic_deficon);
        }
    }

}
