package com.st.stmusic.entity;

import static com.st.stmusic.R.drawable.ic_property_1_pause;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.squareup.picasso.Picasso;
import com.st.stmusic.HomeActivity;
import com.st.stmusic.PlayerActivity;
import com.st.stmusic.R;
import com.st.stmusic.adapter.SelectPlaylistAdapter;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.fragments.PlaylistFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerService implements Serializable {
    public static ExoPlayer player;


    public static void setPlayerActivity(PlayerActivity playerActivity) {
        PlayerService.playerActivity = playerActivity;
    }

    static PlayerActivity playerActivity;

    static PlaylistFragment playlistFragment;

    static HomeActivity activity;

    public PlayerService(HomeActivity activity) {
        PlayerService.activity = activity;
    }

    public int getPos() {
        return pos;
    }

    public ArrayList<MusicModel> getModel() {
        return model;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public TinyDB getTinydb() {
        return tinydb;
    }

    public Context getmContext() {
        return mContext;
    }

    public static ExoPlayer getPlayer() {
        return player;
    }

    static int pos;
    String minute;
    String second;
    String minute2;
    String second2;
    boolean playerSwitch;

    public static String getHoverOnClick() {
        return hoverOnClick;
    }

    static String hoverOnClick;
    static ArrayList<MusicModel> model;
    ArrayList<String> favorites;
    TinyDB tinydb;
    Context mContext;

    public PlayerService() {
    }

    public static void setPos(int pos) {


        PlayerService.pos = pos;

    }

    public PlayerService(int pos, String minute, String second, String minute2, String second2, boolean playerSwitch, ArrayList<MusicModel> model, ArrayList<String> favorites, TinyDB tinydb, Context mContext) {
        PlayerService.pos = pos;
        this.minute = minute;
        this.second = second;
        this.minute2 = minute2;
        this.second2 = second2;
        this.playerSwitch = playerSwitch;
        PlayerService.model = model;
        this.favorites = favorites;
        this.tinydb = tinydb;
        this.mContext = mContext;
    }

    public void setupExo() {
        if (!hoverOnClick.equals("hover")){
            if (player != null) {
                player.stop();
                player = new ExoPlayer.Builder(mContext).build();
            } else {
                player = new ExoPlayer.Builder(mContext).build();
            }
            hoverOnClick="empty";
        }



    }

    public void showCreatePLaylistDialog(){

        View alertView = LayoutInflater.from(playerActivity).inflate(R.layout.alert_create_playlist, null);

        EditText txtName = alertView.findViewById(R.id.txtPlaylistNameEdit);
        TextView txtNamePlaylist = alertView.findViewById(R.id.txtPlaylistName);
        Button btnOk = alertView.findViewById(R.id.btnCreate);
        final Dialog ad = new Dialog(playerActivity, R.style.MY_AlertDialog);
        ad.setContentView(alertView);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtName.getText().toString().trim().equals("")) {
                    boolean sit = playerActivity.manager.createPlaylist(txtName.getText().toString().trim());
                    if (sit) {
                        Toast.makeText(playerActivity, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(playerActivity, "failed", Toast.LENGTH_SHORT).show();
                    }
                    ad.dismiss();//dialog dismiss
                } else {
                    Toast.makeText(playerActivity, "Please Enter a Name", Toast.LENGTH_SHORT).show();
                    txtNamePlaylist.setText("Please Enter a Name");
                }
            }
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                txtNamePlaylist.setText(s);
                if (count == 0) {
                    txtNamePlaylist.setText("Please Enter a Name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        ad.create();
        ad.show();

    }

    public void shuffleOn(){
        player.setShuffleModeEnabled(!player.getShuffleModeEnabled());

    }

    public static void setHoverOnClick(String hoverOnClick) {
        PlayerService.hoverOnClick = hoverOnClick;
    }

    public void start() {

        if (!hoverOnClick.equals("hover")){
            for (int i = pos; i < model.size(); i++) {
                player.addMediaItem(i, MediaItem.fromUri(Uri.parse(model.get(i).getmData())));
                player.setShuffleModeEnabled(true);
            }
            player.prepare();
            player.play();
            hoverOnClick="empty";
        }



    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void stopExo() {
        player.stop();
    }

    public void startTransition() {
        activity.binding.motionLayout.transitionToStart();
        activity.transitionStart();

    }

    public void loadHoverData() {
        Picasso.get()
                .load(model.get(pos).getSongImage())
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(activity.binding.imgSongImageHover);
        activity.binding.txtArtistSongs2.setText(model.get(pos).getmArtist());
        activity.binding.txtSongName3.setText(model.get(pos).getmTitle());
        activity.binding.hoverPlayPause.setImageResource(R.drawable.ic_pause);
    }

    public void clickPlayFromPlayerActivity() {
        if (player.isPlaying()) {
            playerSwitch = false;
            player.pause();
            playerActivity.binding.imgPlayPause.setImageResource(R.drawable.ic_property_1_play);
            return;
        } else if (player.isPlaying() == false) {
            playerSwitch = true;
            player.play();
            playerActivity.binding.imgPlayPause.setImageResource(ic_property_1_pause);
            return;

        }
    }

    public void clickPlay() {
        if (player.isPlaying()) {
            playerSwitch = false;
            player.pause();
            activity.binding.hoverPlayPause.setImageResource(R.drawable.ic_play);


            return;
        } else if (player.isPlaying() == false) {
            playerSwitch = true;
            player.play();
            activity.binding.hoverPlayPause.setImageResource(R.drawable.ic_pause);


            return;

        }
    }

    public static void setPlayer(ExoPlayer player) {
        PlayerService.player = player;
    }

    public void next() {

        if (pos < model.size() - 1) {
            player.stop();
            MediaItem mediaItem = MediaItem.fromUri(model.get(pos + 1).getmData());
            pos++;
            //playerActivity.loadData(pos);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();

        }
    }

    public void previous() {
        if (pos > 0) {
            player.stop();
            MediaItem mediaItem = MediaItem.fromUri(model.get(pos - 1).getmData());
            pos = pos - 1;

            //loadData(pos);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();

        }
    }


}

