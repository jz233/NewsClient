package zjj.app.newsclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import zjj.app.newsclient.fragments.DefaultFragment;
import zjj.app.newsclient.fragments.DomesticFragment;
import zjj.app.newsclient.fragments.InternationalFragment;
import zjj.app.newsclient.fragments.SportsFragment;

public class NewsPagerAdapter extends FragmentStatePagerAdapter{

    private String[] topics;

    public NewsPagerAdapter(FragmentManager fm, String[] topics) {
        super(fm);
        this.topics = topics;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return DomesticFragment.newInstance();
            case 1:
                return InternationalFragment.newInstance();
            case 2:
                return SportsFragment.newInstance();
        }
        return DefaultFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return topics[position];
    }

    @Override
    public int getCount() {
        return topics.length;
    }
}
