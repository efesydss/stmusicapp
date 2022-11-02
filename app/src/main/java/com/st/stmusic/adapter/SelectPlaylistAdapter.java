package com.st.stmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.st.stmusic.R;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.SelectPlaylistCardBinding;
import com.st.stmusic.databinding.SongCardBinding;
import com.st.stmusic.entity.PlaylistManager;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;

public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.PlayCardViewHolder> {
    private final Context mContext;
    private final ArrayList<String> playlistList;
    private final TinyDB tinyDB;
    private final String data;

    public SelectPlaylistAdapter(Context mContext, ArrayList<String> playlistList, TinyDB tinyDB,String data) {
        this.mContext = mContext;
        this.playlistList = playlistList;
        this.tinyDB = tinyDB;
        this.data=data;

    }
    public class PlayCardViewHolder extends RecyclerView.ViewHolder{
        SelectPlaylistCardBinding itemView;

        public PlayCardViewHolder(@NonNull SelectPlaylistCardBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
        }
    }

    @NonNull
    @Override
    public PlayCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        SelectPlaylistCardBinding binding = SelectPlaylistCardBinding.inflate(layoutInflater, parent, false);

        return new SelectPlaylistAdapter.PlayCardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayCardViewHolder holder, int position) {

        String str=playlistList.get(position);
        PlaylistManager manager=new PlaylistManager();
        SelectPlaylistCardBinding t = holder.itemView;
        manager.setTinyDB(tinyDB);
        t.txtManySelect.setText(String.valueOf(manager.getManySpecificSize(str)));
        t.txtSelectPlaylistName.setText(str);

        t.selectPlaylistCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " başarılı tıklama ", Toast.LENGTH_SHORT).show();

                if (t.imgLineSelected.getVisibility()==View.INVISIBLE){
                    t.imgLineSelected.setVisibility(View.VISIBLE);
                    manager.addSongsToPlaylist(str,data);//test et
                }else{
                    t.imgLineSelected.setVisibility(View.INVISIBLE);
                    //work%100
                    manager.removeSongsFromPlaylist(str,data);



                }




            }
        });

    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }




}
