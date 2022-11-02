package com.st.stmusic;

import static com.st.stmusic.R.drawable.ic_property_1_pause;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.st.stmusic.adapter.SelectPlaylistAdapter;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.ActivityPlayerBinding;
import com.st.stmusic.entity.PlayerService;
import com.st.stmusic.entity.PlaylistManager;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    public ActivityPlayerBinding binding;
    static int pos;
    static String minute = "";
    static String second = "";
    static String minute2 = "";
    static String second2 = "";
    public static Context myContext;
    static boolean playerSwitch = true;
    ArrayList<MusicModel> model = new ArrayList<MusicModel>();
    ArrayList<String> favorites = new ArrayList<>();//get
    ExoPlayer player;
    TinyDB tinydb;//get
    public PlaylistManager manager;
    ArrayList<String> temp = new ArrayList<>();
    PlayerService activeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        model = getIntent().getExtras().getParcelableArrayList("items");
        int position = Integer.valueOf(getIntent().getExtras().getInt("pos"));
        pos = position;

        setSupportActionBar(binding.toolbarPLayer);

        myContext = getApplicationContext();
        tinydb = new TinyDB(getApplicationContext());


        /****/
        manager = new PlaylistManager(tinydb, getApplicationContext());
        manager.getPlaylist();
        /****/


        getDatabaseFav();



        activeService
                = new PlayerService(pos
                , "", "", "", ""
                , true, model, favorites, tinydb, getApplicationContext());


        PlayerService.setPlayerActivity(PlayerActivity.this);


        activeService.setupExo();

        player = PlayerService.getPlayer();


        loadData(activeService.getPos());

        calculate(player);


        PlayerService.setPos(pos);


        activeService.start();


        activeService.startTransition();

        activeService.loadHoverData();
        Log.e("logw", " pos => " + pos);

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.player_activity_toolbar_menu, menu);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_addto_playlist:

                        show();
                        break;
                    case R.id.action_setas_ringtone:
                        Toast.makeText(PlayerActivity.this, "Set as Ringtone", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_settings:

                        Toast.makeText(PlayerActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


        Handler mHandler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    int mCurrentPosition = Math.toIntExact(player.getCurrentPosition());
                    binding.seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1);
            }
        });


        binding.imgPlayPause.setImageResource(ic_property_1_pause);

        binding.imgAddFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnImageFavorite();
            }
        });


        binding.imgShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeService.shuffleOn();//çalışmıyor aq
                Log.e("shuffle", "shuffle mode " + player.getShuffleModeEnabled());

            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress == binding.seekBar.getMax()) {


                    next(player);
                }
                if (player != null && fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                player.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (playerSwitch) {
                    player.play();
                }


            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos < model.size() - 1) {
                    player.stop();
                    MediaItem mediaItem = MediaItem.fromUri(model.get(pos + 1).getmData());
                    pos++;
                    loadData(pos);
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.play();

                    PlayerService.setPos(pos);
                }
                /**
                 * if (pos < model.size() - 1) {
                 if (player != null) {

                 player.stop();
                 MediaItem mediaItem = MediaItem.fromUri(model.get(pos + 1).getmData());
                 pos++;
                 //loadData(pos);
                 player.setMediaItem(mediaItem);
                 player.prepare();
                 player.play();
                 }


                 }**/


            }
        });
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos > 0) {
                    player.stop();
                    MediaItem mediaItem = MediaItem.fromUri(model.get(pos - 1).getmData());
                    pos = pos - 1;

                    loadData(pos);
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.play();
                    PlayerService.setPos(pos);
                } else {
                }


            }
        });


        binding.imgPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeService.clickPlayFromPlayerActivity();


            }
        });
    }


    public ArrayList<String> getAllSongsName() {
        ArrayList<String> liste = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) {

            liste.add(model.get(i).getmTitle());
        }
        return liste;
    }

    public void getDatabaseFav() {
        for (int i = 0; i < tinydb.getListString("favorites").size(); i++) {
            favorites.add(tinydb.getListString("favorites").get(i));
        }
    }

    public void addDatabaseFav(String data) {
        favorites.add(data);
        tinydb.putListString("favorites", favorites);

    }


    public void removeDatabaseFav() {
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).equals(model.get(pos).getmData())) {
                favorites.remove(i);
                tinydb.putListString("favorites", favorites);


            }
        }
    }

    public void show() {
        //activeService.showCreatePLaylistDialog();
        View alertView = LayoutInflater.from(PlayerActivity.this).inflate(R.layout.alert_add_song_playlist, null);
        RecyclerView rv = alertView.findViewById(R.id.recyclerViewSpecific);

        rv.setLayoutManager(new LinearLayoutManager(PlayerActivity.this));
        //listeyi ayarla


        temp = manager.getPlaylist(false);
        Button btn = alertView.findViewById(R.id.btnDismiss);

        SelectPlaylistAdapter adapter = new SelectPlaylistAdapter(PlayerActivity.this, temp, tinydb, model.get(pos).getmData());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        final Dialog ad = new Dialog(PlayerActivity.this, R.style.MY_AlertDialog);
        ad.setContentView(alertView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad.dismiss();
            }
        });
        ad.create();
        ad.show();
    }


    public ArrayList<String> getAllSongData() {
        ArrayList<String> listeData = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) {
            listeData.add(model.get(i).getmData());

        }
        return listeData;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void next(ExoPlayer player) {
        if (pos < model.size() - 1) {
            player.stop();
            MediaItem mediaItem = MediaItem.fromUri(model.get(pos + 1).getmData());
            pos++;
            loadData(pos);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }
    }


    public void calculate(ExoPlayer player) {
        final int[] mCurrentPosition2 = new int[1];
        Handler mHandler2 = new Handler();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mCurrentPosition2[0] = Integer.valueOf(model.get(pos).getDuration()) - Math.toIntExact(player.getCurrentPosition());


                mHandler2.postDelayed(this::run, 0);
            }
        });


        Handler mHandler = new Handler();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                int mCurrentPosition = Math.toIntExact(player.getCurrentPosition());
                minute = String.valueOf(Integer.valueOf(mCurrentPosition) / 60000);
                second = String.valueOf((Integer.valueOf(mCurrentPosition) % 60000) / 1000);
                if (Integer.valueOf(second) < 10 && Integer.valueOf(minute) >= 10) {

                    binding.txtGecen.setText(minute + ":" + "0" + second);

                } else if (Integer.valueOf(second) >= 10 && Integer.valueOf(minute) < 10) {

                    binding.txtGecen.setText("0" + minute + ":" + second);

                } else if (Integer.valueOf(second) >= 10 && Integer.valueOf(minute) >= 10) {

                    binding.txtGecen.setText(minute + ":" + second);

                } else if (Integer.valueOf(second) < 10 && Integer.valueOf(minute) < 10) {
                    binding.txtGecen.setText("0" + minute + ":" + "0" + second);
                }

                minute2 = String.valueOf(Integer.valueOf(mCurrentPosition2[0]) / 60000);
                second2 = String.valueOf((Integer.valueOf(mCurrentPosition2[0]) % 60000) / 1000);


                if (Integer.valueOf(second2) < 10 && Integer.valueOf(minute2) >= 10) {

                    binding.txtKalan.setText(minute2 + ":" + "0" + second2);

                } else if (Integer.valueOf(second2) >= 10 && Integer.valueOf(minute2) < 10) {

                    binding.txtKalan.setText("0" + minute2 + ":" + second2);

                } else if (Integer.valueOf(second2) >= 10 && Integer.valueOf(minute2) >= 10) {

                    binding.txtKalan.setText(minute2 + ":" + second2);

                } else if (Integer.valueOf(second2) < 10 && Integer.valueOf(minute2) < 10) {
                    binding.txtKalan.setText("0" + minute2 + ":" + "0" + second2);
                }

                mHandler.postDelayed(this::run, 1000);
            }
        });

    }

    public void btnImageFavorite() {
        if (model.get(pos).getIsLiked() == 0) {

            binding.imgAddFavoritesButton.setImageResource(R.drawable.ic_liked);
            model.get(pos).setIsLiked(1);
            addDatabaseFav(model.get(pos).getmData());

        } else {

            binding.imgAddFavoritesButton.setImageResource(R.drawable.ic_heart);
            model.get(pos).setIsLiked(0);
            removeDatabaseFav();


        }
    }


    public void loadData(int position2) {

        binding.seekBar.setMax(Integer.parseInt(model.get(position2).getDuration()));
        binding.txtSongNameBig.setText(model.get(position2).getmTitle());
        binding.txtArtistName.setText(model.get(position2).getmArtist());
        binding.imgBig.setImageURI(model.get(position2).getSongImage());

        binding.imgAddFavoritesButton.setImageResource(R.drawable.ic_heart);
        for (int i = 0; i < tinydb.getListString("favorites").size(); i++) {

            if (tinydb.getListString("favorites").get(i).equals(model.get(pos).getmData())) {

                model.get(pos).setIsLiked(1);
                binding.imgAddFavoritesButton.setImageResource(R.drawable.ic_liked);
            }
        }


    }

}