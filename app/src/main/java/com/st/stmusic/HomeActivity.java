package com.st.stmusic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.st.stmusic.databinding.ActivityHomeBinding;
import com.st.stmusic.entity.PlayerService;
import com.st.stmusic.fragments.ArtistFragment;
import com.st.stmusic.fragments.FavoritesFragment;
import com.st.stmusic.fragments.PlaylistFragment;
import com.st.stmusic.fragments.SongFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnAttachStateChangeListener {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentBaslikArrayList = new ArrayList<>();
    static int firstTimeOpen=0;

    static int state;
    public ActivityHomeBinding binding;
    PlayerService service = new PlayerService();
    public static ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        try{
            service = new PlayerService(HomeActivity.this);


            binding.motionLayout.transitionToEnd();
        }catch (Exception e){

        }



        binding.constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.setHoverOnClick("hover");

                startActivities(new Intent[]{new Intent(getApplicationContext(), PlayerActivity.class).putParcelableArrayListExtra("items", service.getModel()).putExtra("pos", service.getPos()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)});

            }
        });
        binding.hoverPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstTimeOpen=1;
                service.clickPlay();
            }
        });
        binding.hoverNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTimeOpen=1;
                service.next();
                service.loadHoverData();
            }
        });
        binding.hoverPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTimeOpen=1;
                service.previous();
                service.loadHoverData();
            }
        });


        binding.motionLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                Log.e("motion", "started");
                firstTimeOpen=1;
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                Log.e("motion", "change");
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                Log.e("motion", "completed");
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

                Log.e("motion", "triggered");
            }
        });


        setSupportActionBar(binding.toolbarPlaylist2);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(this);

        fragmentArrayList.add(new PlaylistFragment());
        fragmentArrayList.add(new ArtistFragment());
        fragmentArrayList.add(new SongFragment());
        fragmentArrayList.add(new FavoritesFragment());


        binding.homeVp.setAdapter(adapter);

        fragmentBaslikArrayList.add("Playlist");
        fragmentBaslikArrayList.add("Artist");
        fragmentBaslikArrayList.add("Songs");
        fragmentBaslikArrayList.add("Favorites");

        new TabLayoutMediator(binding.tabLayout, binding.homeVp, ((tab, position) -> tab.setText(fragmentBaslikArrayList.get(position)))).attach();


        binding.homeVp.getCurrentItem();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tab.getText().toString().trim().equals("Playlist")) {

                    state = 0;


                    invalidateOptionsMenu();

                    Log.e("invalidate", "state 0 ");


                } else if (tab.getText().toString().trim().equals("Songs")) {
                    state = 2;


                    invalidateOptionsMenu();

                    Log.e("invalidate", "state 2 ");
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

    }


    /**
     * bu onCreate Metodu çalışmıyor işlevsiz yani fragment kendisi belirliyor hangi menünün toolbara inflate olacağına çok şaşkınım
     **/

    public void transitionStart() {
        binding.motionLayout.transitionToEnd();

    }


    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }

    private class MyViewPagerAdapter extends FragmentStateAdapter {

        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {

            super(fragmentActivity);
        }


        /**
         * fragment changed gibi bir method lazım !
         **/
        @NonNull
        @Override
        public Fragment createFragment(int position) {

            return fragmentArrayList.get(position);

        }


        @Override
        public int getItemCount() {
            return fragmentArrayList.size();
        }
    }

    public void ReloadHover() {
        service.loadHoverData();
    }

    @Override
    protected void onResume() {

        if (firstTimeOpen==1) {
            ReloadHover();
        }
        binding.motionLayout.transitionToStart();
        super.onResume();
    }
}