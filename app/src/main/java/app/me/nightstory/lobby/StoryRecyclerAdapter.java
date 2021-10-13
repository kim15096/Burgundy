package app.me.nightstory.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<StoryPostModel> chatList;


    public StoryRecyclerAdapter(List<StoryPostModel> chatList){
        this.chatList = chatList;

    }

    private class VIEW_TYPES {
        public static final int User = 1;
        public static final int Continued = 2;
    }



    @Override
    public int getItemViewType(int position) {

        if(position > 1 && chatList.get(position).getPpURL()!=null && chatList.get(position).getPpURL().equals(chatList.get(position-1).getPpURL())){
            return VIEW_TYPES.Continued;
        }
        else {
            return VIEW_TYPES.User;
        }


    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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



}
