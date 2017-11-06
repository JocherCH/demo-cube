package com.rider.democube;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.rider.democube.entity.PageModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;

    ViewPager viewPager;

    List<PageModel> pageModels = new ArrayList<>();


    {
        pageModels.add(new PageModel(R.layout.sample_square_image_view,R.string.title_square_image_view,R.layout.practice_square_image_view));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);

                return PageFragment.newInstance(pageModel.getSampleLayoutRes(),pageModel.getPracticeLayoutRes());
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).getTitleRes());
            }
        });
        tabLayout.setupWithViewPager(viewPager);

    }

}
