package com.st.stmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.PlaylistCardBinding;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private final Context mContex;
    private final List<MusicModel> musicModelList;


    public MusicAdapter(Context mContex, List<MusicModel> musicModelList) {
        this.mContex = mContex;
        this.musicModelList = musicModelList;
    }
    public class MusicViewHolder extends RecyclerView.ViewHolder{
        PlaylistCardBinding itemView;

        public MusicViewHolder(@NonNull PlaylistCardBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;

        }
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContex);
        PlaylistCardBinding binding = PlaylistCardBinding.inflate(layoutInflater, parent, false);

        return new MusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

        MusicModel model= musicModelList.get(position);
        PlaylistCardBinding t=holder.itemView;
        t.txtMany.setText("1");
        t.txtName.setText("Playlist");

    }

    @Override
    public int getItemCount() {
        return musicModelList.size();
    }



}
