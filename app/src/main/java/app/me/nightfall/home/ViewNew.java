package app.me.nightfall.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import app.me.nightfall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNew extends Fragment {


    public ViewNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.vp_new, container, false);
    }

}
