package assignment.example.com.easystock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Víctor on 19/11/2017.
 * This class is required to use fragments. It implements FragmentPagerAdapter.
 * Reference: I learned to implement fragments by watching this tutorial: https://www.youtube.com/watch?v=q1Kra_bQDOk&t=544s.
 */

public class SectionPageAdapter extends FragmentPagerAdapter{
    private final List<Fragment> mFragmentList= new ArrayList<>();
    private final List<String> mFragmentTitleList= new ArrayList<>();

    //Function to add a fragment
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);//add the fragment
        mFragmentTitleList.add(title);//add the fragment's title
    }

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position );
    }

    @Override
    public int getCount() {

        return mFragmentList.size();
    }
}
