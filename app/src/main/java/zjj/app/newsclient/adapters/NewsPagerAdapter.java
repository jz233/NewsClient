package zjj.app.newsclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import zjj.app.newsclient.fragments.DefaultFragment;
import zjj.app.newsclient.fragments.NewsPagerFragment;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.SharedPreferencesUtils;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private String internationalId;
    private String eduId;
    private String[] topics;
    private String domesticId;

    public NewsPagerAdapter(AppCompatActivity context, FragmentManager fm, String[] topics, int type) {
        super(fm);
        this.topics = topics;
        if(type == 0){
            domesticId = SharedPreferencesUtils.getString(context, "国内焦点", Constant.DEFAULT_CHANNEL_ID);
            internationalId = SharedPreferencesUtils.getString(context, "国际焦点", Constant.DEFAULT_CHANNEL_ID);
            eduId = SharedPreferencesUtils.getString(context, "教育焦点", Constant.DEFAULT_CHANNEL_ID);
        }else if(type == 1){
            domesticId = SharedPreferencesUtils.getString(context, "国内最新", Constant.DEFAULT_CHANNEL_ID);
            internationalId = SharedPreferencesUtils.getString(context, "国际最新", Constant.DEFAULT_CHANNEL_ID);
            eduId = SharedPreferencesUtils.getString(context, "教育最新", Constant.DEFAULT_CHANNEL_ID);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NewsPagerFragment.newInstance(domesticId, "1");
            case 1:
                return NewsPagerFragment.newInstance(internationalId, "1");
            case 2:
                return NewsPagerFragment.newInstance(eduId, "1");
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
