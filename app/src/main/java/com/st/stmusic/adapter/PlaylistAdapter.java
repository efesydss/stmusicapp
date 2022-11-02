package com.st.stmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.st.stmusic.PlaylistDetailActivity;
import com.st.stmusic.R;
import com.st.stmusic.databinding.PlaylistCardBinding;
import com.st.stmusic.entity.PlaylistManager;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MusicViewHolder> {
    private final Context mContext;
    private final ArrayList<String> musicModelList;
    private final int playListSize;
    private final TinyDB tinyDB;


    public PlaylistAdapter(Context mContext, ArrayList<String> musicModelList, int playListSize, TinyDB tinyDB) {
        this.mContext = mContext;
        this.musicModelList = musicModelList;
        this.playListSize = playListSize;
        this.tinyDB = tinyDB;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        PlaylistCardBinding itemView;

        public MusicViewHolder(@NonNull PlaylistCardBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        PlaylistCardBinding binding = PlaylistCardBinding.inflate(layoutInflater, parent, false);


        return new MusicViewHolder(binding);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {


        PlaylistManager manager = new PlaylistManager(tinyDB, mContext);
        String str = musicModelList.get(position);
        PlaylistCardBinding t = holder.itemView;
        t.txtMany.setText(String.valueOf(manager.getManySpecificSize(str))+" Song");
        t.txtName.setText(str);
        manager.setTinyDB(tinyDB);


        t.playlistCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mContext.startActivities(new Intent[]{new Intent(mContext, PlaylistDetailActivity.class).putExtra("currentPlaylist", str)});

                }catch (Exception e){

                }
            }
        });


        t.imgMoreVert2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                menuInflater.inflate(R.menu.playlist_card_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_remove:
                                try{
                                    Toast.makeText(mContext, "removed", Toast.LENGTH_SHORT).show();
                                    manager.removePlaylist(str);
                                    Log.e("ctrla", "adapterin yolladığı playlistName 'str' => " + str);


                                    musicModelList.remove(position);
                                    //burada kayıtlı songdataları da sil ilgili
                                    notifyItemRemoved(position);
                                }catch (Exception e){

                                }

                                return true;

                        }
                        return false;
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {

        return musicModelList.size();

    }


}
