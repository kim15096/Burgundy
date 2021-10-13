package app.me.nightstory.article;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.DocumentTransform;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class ArticleRecyclerAdapter extends FirestoreRecyclerAdapter<ArticlePostModel, ArticleViewHolder> {

    public Context context;
    private final String userID;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final FirebaseUser firebaseUser;
    private final DocumentReference userRef;


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
        //holder.setLikeCount(model.getLikes());

        holder.getLikeStatus(model.getPostID());

        if (model.getLikes()>=50){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Posts").document(model.getPostID()).collection("Likes").document(MainActivity.myUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            db.collection("Posts").document(model.getPostID()).collection("Likes").document(MainActivity.myUserID).delete();
                        }
                        else {
                            LottieAnimationView thumbsup = holder.itemView.findViewById(R.id.thumbsup_animation);
                            thumbsup.setVisibility(View.VISIBLE);
                            thumbsup.playAnimation();
                            thumbsup.addAnimatorListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    thumbsup.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            Map<String, Object> userLike = new HashMap<>();
                            userLike.put("status", true);
                            db.collection("Posts").document(model.getPostID()).collection("Likes").document(MainActivity.myUserID).set(userLike);
                        }
                    }
                });
            }
        });

        //deleting posts admin
        final Button[] delBtn = {holder.itemView.findViewById(R.id.adminDelBtn)};
        delBtn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Posts").document(model.getPostID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });

            }
        });


        //


        holder.postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView content = holder.itemView.findViewById(R.id.postrecycler_content);
                ImageView btn = holder.itemView.findViewById(R.id.postL_btn);

                System.out.println(content.getLineCount());
                int cardH = holder.postCard.getHeight();

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




        db.collection("Users").document(model.getPosterID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!(documentSnapshot.get("imageURL") ==null)) {
                    String imageUrl = documentSnapshot.get("imageURL").toString();
                    holder.setProfilePicture(imageUrl);
                }
                else {

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
