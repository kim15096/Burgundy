package app.me.nightfall.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.me.nightfall.R;

public class    ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<ChatPostModel> chatList;


    public ChatRecyclerAdapter(List<ChatPostModel> chatList){
        this.chatList = chatList;

    }

    private class VIEW_TYPES {
        public static final int User = 1;
        public static final int Other = 2;
    }



    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getSenderID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return VIEW_TYPES.User;
        }

        else {
            return VIEW_TYPES.Other;

        }
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View UserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_user, parent, false);
        View OtherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_other, parent, false);

        if (viewType == VIEW_TYPES.User){
            return new UserViewHolder(UserView);
        }
        else if(viewType == VIEW_TYPES.Other){
            return new OtherViewHolder(OtherView);
        }
        else{
            return new UserViewHolder(UserView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.User:
                UserViewHolder userViewHolder = (UserViewHolder) holder;

                String message = chatList.get(position).getMessage();
                userViewHolder.setMessage(message);



                break;
            case VIEW_TYPES.Other:
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;

                String message_other = chatList.get(position).getMessage();
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

    public class UserViewHolder extends RecyclerView.ViewHolder{


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);


        }
        public void setMessage(String text){
            TextView title_tv = itemView.findViewById(R.id.chat_message);
            title_tv.setText(text);

        }


    }
    public class OtherViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        public OtherViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

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
