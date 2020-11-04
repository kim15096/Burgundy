package app.me.nightfall.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.me.nightfall.R;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<StoryPostModel> chatList;


    public StoryRecyclerAdapter(List<StoryPostModel> chatList){
        this.chatList = chatList;

    }

    private class VIEW_TYPES {
        public static final int User = 1;
        public static final int Other = 2;
        public static final int Bot = 3;
    }



    @Override
    public int getItemViewType(int position) {

            return VIEW_TYPES.User;

    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View UserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_recycler_post, parent, false);
        View OtherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_other, parent, false);

        if (viewType == VIEW_TYPES.User){
            return new UserViewHolder(UserView);
        }
        else {
            return new OtherViewHolder(OtherView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.User:
                UserViewHolder userViewHolder = (UserViewHolder) holder;

                String message = chatList.get(position).getText();
                userViewHolder.setMessage(message);



                break;
            case VIEW_TYPES.Other:
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;

                String message_other = chatList.get(position).getText();
                String sender_other = chatList.get(position).getUsername();

                if (position!=0) {
                    String sender_other_prev = chatList.get(position - 1).getUsername();
                    if (!sender_other_prev.equals(sender_other)){
                        otherViewHolder.setUsername(sender_other);

                    }
                    else{
                        otherViewHolder.setUsername("");
                    }
                }
                else {
                    otherViewHolder.setUsername(sender_other);
                }
                otherViewHolder.setMessage(message_other);

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
    public static class OtherViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        public OtherViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }
        public void setColor(String text){
            CardView cardView = itemView.findViewById(R.id.chatOther_card);


        }

        public void setMessage(String text){
            TextView title_tv = itemView.findViewById(R.id.chat_message_other);
            title_tv.setText(text);

        }

        public void setUsername(String text){

            TextView sender = itemView.findViewById(R.id.chatOther_tv);

            if (text.equals("")){
                sender.setVisibility(View.GONE);
            }
            else {
                sender.setVisibility(View.VISIBLE);
                sender.setText(text);

            }
        }




    }


}
