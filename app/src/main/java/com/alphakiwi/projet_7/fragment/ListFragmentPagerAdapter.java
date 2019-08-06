package com.alphakiwi.projet_7.fragment;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ListFragmentPagerAdapter extends FragmentPagerAdapter {

    public ListFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return FirstFragment.newInstance();
        }else if (position == 1) {
            return SecondFragment.newInstance();
        }else{
            return ThirdFragment.newInstance();
        }
    }


    /**
     * get the number of pages
     * @return
     */
    @Override
    public int getCount() {
        return 3;
    }
}



