package app.me.nightfall.addpost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.me.nightfall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFrag extends Fragment {

    public PostFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);







        return view;
    }
}
