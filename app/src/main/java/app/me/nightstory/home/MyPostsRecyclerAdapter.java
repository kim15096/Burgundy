package app.me.nightstory.home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.me.nightstory.R;
import app.me.nightstory.article.ArticlePostModel;
import app.me.nightstory.article.ArticleViewHolder;
import app.me.nightstory.lobby.LobbyActivity;

public class MyPostsRecyclerAdapter extends FirestoreRecyclerAdapter<ArticlePostModel, ArticleViewHolder> {

    public Context context;
    private final String userID;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final FirebaseUser firebaseUser;
    private final DocumentReference userRef;
    private Boolean checkU = false;
    private Boolean checkD = false;

    public MyPostsRecyclerAdapter(FirestoreRecyclerOptions recyclerOptions) {
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
        holder.setProfilePicture(MainActivity.myPpURL);

        TextView delBtn = holder.itemView.findViewById(R.id.postL_delBtn);
        TextView likeBtn = holder.itemView.findViewById(R.id.postL_likeBtn);
        delBtn.setVisibility(View.VISIBLE);
        likeBtn.setVisibility(View.INVISIBLE);

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext(), R.style.dialogTheme)
                        .setTitle("게시물을 삭제합니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Posts").document(model.getPostID())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db.collection("Users").document(firebaseUser.getUid()).collection("MyPosts").document(model.getPostID())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });
                                            }
                                        });
                            }
                        })

                        .setNegativeButton("취소", null)
                        .show();


            }
        });


        holder.postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView content = holder.itemView.findViewById(R.id.postrecycler_content);
                ImageView btn = holder.itemView.findViewById(R.id.postL_btn);

                if (content.getMaxLines() == 3){
                    content.setMaxLines(100);
                    rotateAnimate(btn, 0f, 180f).start();


                }
                else{
                    content.setMaxLines(3);
                    rotateAnimate(btn, 180f, 0f).start();
                }


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

    private ObjectAnimator rotateAnimate(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(400);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

}
