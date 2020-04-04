package app.me.nightfall.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FeaturedLobbies(); //ChildFragment1 at position 0
            case 1:
                return new NormalLobbies(); //ChildFragment2 at position 1
            case 2:
                return new CategoriesFrag();
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 3; //two fragments
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Featured";

            case 1:
                return "Lobbies";

            case 2:
                return "Categories";

        }
        return null;
    }
}




