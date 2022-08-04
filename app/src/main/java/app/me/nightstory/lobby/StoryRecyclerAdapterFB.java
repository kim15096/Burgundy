package app.me.nightstory.lobby;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import app.me.nightstory.home.MainActivity;

public class StoryRecyclerAdapterFB extends FirestoreRecyclerAdapter<StoryPostModel, StoryViewHolder> {

    public Context context;
    private final String userID;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final FirebaseUser firebaseUser;
    private final DocumentReference userRef;

    public StoryRecyclerAdapterFB(FirestoreRecyclerOptions recyclerOptions) {
        super(recyclerOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();
        userRef = db.collection("Users").document(userID);

    }

    @Override
    protected void onBindViewHolder(StoryViewHolder holder, final int position, final StoryPostModel model) {

        holder.setMessage(model.getText());
        holder.setProfilePicture(model.getPpURL());
        holder.setUsername(model.getChatUsername());

        if(model.getTimestamp()!=null){
            holder.setTime(model.getTimestamp().getTime());
        }


        TextView replyView = holder.itemView.findViewById(R.id.answer_reply);
        TextView textView = holder.itemView.findViewById(R.id.see_reply_text);

       if (userID.equals(LobbyActivity.hostID)){
            replyView.setVisibility(View.VISIBLE);
        }
        else {
            replyView.setVisibility(View.GONE);
        }

        String reply = model.getResponse();
        if (reply.equals("")){
            textView.setClickable(false);
            textView.setEnabled(false);
            textView.setTextColor(Color.GRAY);
        }
        else {
            textView.setClickable(true);
            textView.setEnabled(true);
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        replyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnsweringTab(model.getCommentID());
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReply(reply);
            }
        });


    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_recycler_post, parent, false);

        return new StoryViewHolder(view);
    }

    private void openReply(String reply){



        new AlertDialog.Builder(context)
                .setTitle("답장")
                .setMessage(reply)
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }

    @SuppressLint("RestrictedApi")
    private void openAnsweringTab(String docID) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.dialogTheme);

        final EditText edittext = new EditText(context);
        alert.setTitle("답장하기");

        edittext.setBackground(context.getResources().getDrawable(R.drawable.et_bg3));
        edittext.setTextSize(16);
        edittext.setTextColor(context.getResources().getColor(R.color.textColorBlack));

        alert.setView(edittext, 50, 50, 50, 0);

        alert.setPositiveButton("답장하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String textString = edittext.getText().toString();
                db.collection("Lobbies").document(MainActivity.inLobbyID).collection("Story").document(docID).update("response", textString);

            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

}
