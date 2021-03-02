package app.me.nightstory.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.me.nightstory.R;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<StoryPostModel> chatList;


    public StoryRecyclerAdapter(List<StoryPostModel> chatList){
        this.chatList = chatList;

    }

    private class VIEW_TYPES {
        public static final int User = 1;
    }



    @Override
    public int getItemViewType(int position) {

            return VIEW_TYPES.User;

    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View UserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_recycler_post, parent, false);

        return new UserViewHolder(UserView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.User:
                UserViewHolder userViewHolder = (UserViewHolder) holder;

                String message = chatList.get(position).getText();
                userViewHolder.setMessage(message);



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


    }



}
