package assasingh.nearmev2.Adaptors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import assasingh.nearmev2.R;
import assasingh.nearmev2.View.TrendingFragment;

/**
 * Created by Assa Chana on 08/12/2016.
 */

public class TrendingAdapter extends FragmentStatePagerAdapter {


    public TrendingAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TrendingFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("count", position+1);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
