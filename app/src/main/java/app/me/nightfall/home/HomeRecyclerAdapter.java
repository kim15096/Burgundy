package app.me.nightfall.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.me.nightfall.R;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<HomePostModel> post_list;

    public HomeRecyclerAdapter(List<HomePostModel> post_list){
        this.post_list = post_list;


    }

    private class VIEW_TYPES {
        public static final int Normal = 1;
        public static final int Ads = 2;
    }



    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPES.Normal;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View MainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycler_card, parent, false);
        View AdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycler_ad, parent, false);

        if (viewType == VIEW_TYPES.Normal){
            return new ViewHolder0(MainView);
        }
        else if(viewType == VIEW_TYPES.Ads){
            return new ViewHolderAds(AdView);
        }
        else{
            return new ViewHolder0(MainView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.Ads:
                ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
                break;
            case VIEW_TYPES.Normal:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
        }
    }

    public int getPosition(){
     return 3;
    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder{

        private View mView;

        public ViewHolder0(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

    }

    public class ViewHolderAds extends RecyclerView.ViewHolder{

        private View mView;

        public ViewHolderAds(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

    }

}
