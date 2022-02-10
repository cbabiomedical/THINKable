package com.example.thinkableproject.puzzle.ViewPager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageFragmentCollectionAdapter extends FragmentStatePagerAdapter {
    public ViewPageFragmentCollectionAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        ViewPagerFragment demoFragment=new ViewPagerFragment();
        Bundle bundle =new Bundle();
        position = position+1;
        if(position == 1)
        {
            bundle.putString("massage","3X3");
            bundle.putInt("page",position);
        }
        if(position == 2)
        {
            bundle.putString("massage","4X4");
            bundle.putInt("page",position);
        }
        if(position == 3)
        {
            bundle.putString("massage","5X5");
            bundle.putInt("page",position);
        }
        demoFragment.setArguments(bundle);
        return demoFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
