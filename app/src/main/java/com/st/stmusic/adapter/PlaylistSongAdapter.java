package com.st.stmusic.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.st.stmusic.PlayerActivity;
import com.st.stmusic.R;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.SongCardBinding;
import com.st.stmusic.entity.PlaylistManager;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;

public class PlaylistSongAdapter extends RecyclerView.Adapter<PlaylistSongAdapter.PlaylistSongViewHolder> {
    private final Context mContext;
    private final ArrayList<MusicModel> songsList;
    private TinyDB tinyDB;
    ArrayList<String> temp = new ArrayList<>();

    public PlaylistSongAdapter(Context mContext, ArrayList<MusicModel> songsList, TinyDB tinyDB) {
        this.mContext = mContext;
        this.songsList = songsList;
        this.tinyDB = tinyDB;
    }
    public class PlaylistSongViewHolder extends RecyclerView.ViewHolder {
        SongCardBinding itemView;

        public PlaylistSongViewHolder(@NonNull SongCardBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public PlaylistSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        SongCardBinding binding = SongCardBinding.inflate(layoutInflater, parent, false);

        return new PlaylistSongAdapter.PlaylistSongViewHolder(binding);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull PlaylistSongViewHolder holder, int position) {
        MusicModel model = songsList.get(position);


        tinyDB=new TinyDB(mContext);
        PlaylistManager manager = new PlaylistManager(tinyDB,mContext);

        manager.setTinyDB(tinyDB);




        model.setPosition(String.valueOf(position));
        SongCardBinding t = holder.itemView;
        t.txtSongName.setText(model.getmTitle());
        t.txtArtistSongs.setText(model.getmArtist());
        manager.setTinyDB(tinyDB);
        Picasso.get()
                .load(model.getSongImage())
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(t.imgSongImage);


        t.txtDuration.setText(String.valueOf(model.getDuration()));
        t.songcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.startActivities(new Intent[]{new Intent(mContext, PlayerActivity.class).putParcelableArrayListExtra("items", songsList).putExtra("pos", position)});


            }
        });
        t.imgMoreVert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                menuInflater.inflate(R.menu.song_card_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_add_to_queue:
                                Toast.makeText(mContext, model.getmTitle() + " added to Queue", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.action_playfromnow:
                                Toast.makeText(mContext, model.getmTitle() + " play from now", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.action_setasringtone:
                                Toast.makeText(mContext, model.getmTitle() + " setting as ringtone", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.action_addplaylist:

                                // manager.addSongsToPlaylist("efe",model.getmData());
                                View alertView = LayoutInflater.from(mContext).inflate(R.layout.alert_add_song_playlist, null);
                                RecyclerView rv=alertView.findViewById(R.id.recyclerViewSpecific);

                                rv.setLayoutManager(new LinearLayoutManager(mContext));
                                //listeyi ayarla


                                temp= manager.getPlaylist(false);
                                Button btn=alertView.findViewById(R.id.btnDismiss);

                                SelectPlaylistAdapter adapter=new SelectPlaylistAdapter(mContext,temp,tinyDB, model.getmData());
                                rv.setAdapter(adapter);
                                rv.setHasFixedSize(true);

                                final Dialog ad = new Dialog(mContext, R.style.MY_AlertDialog);
                                ad.setContentView(alertView);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ad.dismiss();
                                    }
                                });
                                ad.create();
                                ad.show();

                                return true;
                        }
                        return false;
                    }
                });
            }
        });
        /**modelde music id var onu Shared preferencese kaydet ki favorilerde gözüksün**/
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }
    public ArrayList<MusicModel> getSongsList() {
        return songsList;
    }



}
