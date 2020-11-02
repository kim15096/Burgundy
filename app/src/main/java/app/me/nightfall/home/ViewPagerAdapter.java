package app.me.nightfall.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 2:
                ViewNew viewNew = new ViewNew();
                return viewNew;

            case 1:
                ViewTrend viewTrend = new ViewTrend();
                return viewTrend;

            case 0:
                ViewHot viewHot = new ViewHot();
                return viewHot;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        return null;
    }
}
