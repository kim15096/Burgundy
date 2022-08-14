package app.burgundy.com.lobby;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import app.burgundy.com.R;
import app.burgundy.com.home.MainActivity;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<StoryPostModel> chatList;
    private Context context;
    private FirebaseFirestore db;


    public StoryRecyclerAdapter(List<StoryPostModel> chatList){
        this.chatList = chatList;

    }

    private class VIEW_TYPES {
        public static final int User = 1;
        public static final int Continued = 2;
    }



    @Override
    public int getItemViewType(int position) {

        return VIEW_TYPES.User;


    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();


        View UserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_recycler_post, parent, false);
        View ContinuedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_recycler_post_continued, parent, false);

        if (viewType == VIEW_TYPES.Continued){
            return new UserViewHolder(ContinuedView);
        }
        else if (viewType == VIEW_TYPES.User){
            return new UserViewHolder(UserView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        db = FirebaseFirestore.getInstance();

        db.collection("Lobbies").document(MainActivity.inLobbyID).collection("Story").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

            }
        });

        TextView textView = holder.itemView.findViewById(R.id.see_reply_text);
        String reply = chatList.get(position).getResponse();
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

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openReply(reply);
            }
        });

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.User:
                UserViewHolder userViewHolder = (UserViewHolder) holder;

                String message = chatList.get(position).getText();
                String username = chatList.get(position).getChatUsername();
                String profileURL = chatList.get(position).getPpURL();
                userViewHolder.setMessage(message);
                userViewHolder.setProfilePicture(profileURL);
                userViewHolder.setUsername(username);





                break;

                case VIEW_TYPES.Continued:
                    UserViewHolder userViewHolder2 = (UserViewHolder) holder;
                    String contMessage = chatList.get(position).getText();
                    userViewHolder2.setMessage(contMessage);

                    break;



        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);


        }
        public void setMessage(String text){
            TextView title_tv = itemView.findViewById(R.id.chat_message);
            title_tv.setText(text);

        }

        public void setProfilePicture(String text){
            CircularImageView pp = itemView.findViewById(R.id.chat_pp);
            Glide.with(itemView).load(text).centerCrop()
                    .into(pp);
        }

        public void setUsername(String text){
            TextView userTV = itemView.findViewById(R.id.chat_username);
            userTV.setText(text);
        }


    }

    private void openReply(String reply){



        new AlertDialog.Builder(context)
                .setTitle("답장")
                .setMessage("1`23123")
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



}
