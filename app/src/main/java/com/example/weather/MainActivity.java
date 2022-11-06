package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{ //implements Fragment1.FragmentBListener, Fragment3.FragmentAListener{

    private static final int numPages=3;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;
//    private Fragment3 fragmentA;
//    private Fragment1 fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager2 = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlideAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

//        fragmentA = new Fragment3();
//        fragmentB = new Fragment1();

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.containerA,fragmentA)
//                .replace(R.id.containerB,fragmentB)
//                .commit();
    }

//    @Override
//    public void onInputBSent(CharSequence input) {
//
//    }
//
//    @Override
//    public void onInputASent(CharSequence input) {
//
//    }

    private class ScreenSlideAdapter extends FragmentStateAdapter {
        public ScreenSlideAdapter(MainActivity mainActivity) {
            super(mainActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position){
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
                default:
                    return null;

            }
        }

        @Override
        public int getItemCount() {
            return numPages;
        }
    }
}