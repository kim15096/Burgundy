package app.me.nightstory.article;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class ArticleViewHolder extends RecyclerView.ViewHolder{

    private final View mView;
    public ImageButton thumpsUp;
    public ImageButton thumbsDown;
    public CardView postCard;



    public ArticleViewHolder(@NonNull View itemView) {

        super(itemView);
        mView = itemView;
        thumpsUp = mView.findViewById(R.id.postL_thumpsup);
        thumbsDown = mView.findViewById(R.id.postL_thumbsD);
        postCard = mView.findViewById(R.id.postCardView);

    }

    public void setUsername(String text){
        TextView username = mView.findViewById(R.id.postLay_username);
        username.setText("Posted by " + text);
    }

    public void setTitle(String text){
        TextView title_tv1 = mView.findViewById(R.id.postrecycler_title);
        title_tv1.setText(text);

    }

    public void setContent(String text){
        TextView content_tv2 = mView.findViewById(R.id.postrecycler_content);
        content_tv2.setText(text);
    }

    public void setProfilePicture(String text){
        CircularImageView pp = mView.findViewById(R.id.postLay_pp);
        if (!text.equals("")){
            Glide.with(itemView).load(text).centerCrop().into(pp);
        }
        else {
            pp.setImageResource(R.drawable.ic_deficon);
        }
    }


}
