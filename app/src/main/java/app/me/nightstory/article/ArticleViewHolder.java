package app.me.nightstory.article;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    public CardView postCard;
    private CircularImageView pp;
    public TextView like, likeBtn;
    private int count;


    public ArticleViewHolder(@NonNull View itemView) {

        super(itemView);
        mView = itemView;
        like = mView.findViewById(R.id.postL_like);
        likeBtn = itemView.findViewById(R.id.postL_likeBtn);


        postCard = mView.findViewById(R.id.postCardView);
        pp = mView.findViewById(R.id.postLay_pp);

    }

    public void setUsername(String text) {
        TextView username = mView.findViewById(R.id.postLay_username);
        username.setText(text);
    }

    public void setTitle(String text) {
        TextView title_tv1 = mView.findViewById(R.id.postrecycler_title);
        title_tv1.setText(text);

    }

    public void setContent(String text) {
        TextView content_tv2 = mView.findViewById(R.id.postrecycler_content);
        content_tv2.setText(text);
    }

    public void setProfilePicture(String text) {
        pp = mView.findViewById(R.id.postLay_pp);
        Glide.with(itemView).load(text).centerCrop().into(pp);

    }

    public void setLikeCount(Long text) {
        like.setText(text.toString());
    }

    public void getLikeStatus(String postID) {
        count = 0;
        FirebaseFirestore.getInstance().collection("Posts").document(postID).collection("Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(doc.getDocument().getId())) {
                            likeBtn.setTextColor(mView.getResources().getColor(R.color.colorPrimary));
                        }
                        count++;
                    } else if (doc.getType() == DocumentChange.Type.REMOVED) {
                        count--;
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(doc.getDocument().getId())) {
                            likeBtn.setTextColor(mView.getResources().getColor(R.color.lightGray));
                        }
                    }
                }
                like.setText(Integer.toString(count));


            }


        });
    }

}
