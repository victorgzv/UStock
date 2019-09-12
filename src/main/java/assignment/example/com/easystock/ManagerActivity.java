package assignment.example.com.easystock;

/**
 * Created by Víctor Gonzalez.
 * This class is only for the manager user and it handles fragments. It adds 3 different tabs to this activity
 * Reference: I learned to implement fragments by watching this tutorial: https://www.youtube.com/watch?v=q1Kra_bQDOk&t=544s.
 */


import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class ManagerActivity extends AppCompatActivity {
    private SectionPageAdapter mSectionsPageAdapter;
    private ViewPager mviewPager;
    Database db = new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        //Create sections adapater
        mSectionsPageAdapter= new SectionPageAdapter(getSupportFragmentManager());

        //Set up the viewPager with the section adapter
        mviewPager=(ViewPager) findViewById(R.id.container);

        setupViewPager(mviewPager);
        //Start in the second tab of the fragments
        mviewPager.setCurrentItem(1,false);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);//Add tabs(3 other activities to this one)
        db.open();//Open connection
    }

    //Function that adds the fragments to the main activity
    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter= new SectionPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new tab1Fragment(),"Category ");
        adapter.addFragment(new tab2Fragment(),"Product");
        adapter.addFragment(new tab3Fragment(),"Supplier");
        viewPager.setAdapter(adapter);
    }
}
