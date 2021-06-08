package app.me.nightstory.article;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.me.nightstory.R;

public class ArticleRecyclerAdapter extends FirestoreRecyclerAdapter<ArticlePostModel, ArticleViewHolder> {

    public Context context;
    private final String userID;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final FirebaseUser firebaseUser;
    private final DocumentReference userRef;
    private Boolean checkU = false;
    private Boolean checkD = false;

    public ArticleRecyclerAdapter(FirestoreRecyclerOptions recyclerOptions) {
        super(recyclerOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();
        userRef = db.collection("Users").document(userID);

    }

    @Override
    protected void onBindViewHolder(ArticleViewHolder holder, final int position, final ArticlePostModel model) {
        holder.setTitle(model.getTitle());
        holder.setContent(model.getContent());
        holder.setUsername(model.getPoster());

        holder.thumbsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkD && !checkU)
                {

                    holder.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_filled);
                    checkD = true;
                }
                else if (!checkD && checkU){
                    holder.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_filled);
                    checkD = true;
                    holder.thumpsUp.setImageResource(R.drawable.ic_baseline_thumb_up);
                    checkU = false;
                }
                else
                {
                    holder.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24);
                    checkD = false;

                }
            }
        });

        holder.thumpsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkU && !checkD)
                {

                    holder.thumpsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    checkU = true;
                }
                else if (!checkU && checkD){
                    holder.thumpsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    checkU = true;
                    holder.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24);
                    checkD = false;
                }
                else if (checkU && !checkD)
                {
                    holder.thumpsUp.setImageResource(R.drawable.ic_baseline_thumb_up);
                    checkU = false;

                }
            }


        });

        db.collection("Users").document(model.getPosterID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String imageUrl = documentSnapshot.get("imageURL").toString();
                holder.setProfilePicture(imageUrl);

            }
        });


    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);

        return new ArticleViewHolder(view);
    }

}
