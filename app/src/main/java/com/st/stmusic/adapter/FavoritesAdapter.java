package com.st.stmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.st.stmusic.PlayerActivity;
import com.st.stmusic.R;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.FavoritesCardBinding;
import com.st.stmusic.databinding.SongCardBinding;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private final Context mContext;
    private final ArrayList<MusicModel> songsList;

    public FavoritesAdapter(Context mContext, ArrayList<MusicModel> songsList) {
        this.mContext = mContext;
        this.songsList = songsList;
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        FavoritesCardBinding itemView;

        public FavoritesViewHolder(@NonNull FavoritesCardBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }


    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        FavoritesCardBinding binding = FavoritesCardBinding.inflate(layoutInflater, parent, false);
        return new FavoritesAdapter.FavoritesViewHolder(binding);
    }



    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {

        MusicModel model = songsList.get(position);
        model.setPosition(String.valueOf(position));
        FavoritesCardBinding t = holder.itemView;
        t.txtName.setText(model.getmTitle());
        t.txtArtist.setText(model.getmArtist());
        Picasso.get()
                .load(model.getSongImage())
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(t.imgSongImage);
        t.imgMoreVert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                menuInflater.inflate(R.menu.favorites_card_menu, popupMenu.getMenu());
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
                                Toast.makeText(mContext, model.getmTitle() + " added playlist", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return false;
                    }
                });
            }
        });

        t.txtDuration2.setText(model.getDuration());
        t.favoritesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivities(new Intent[]{new Intent(mContext
                        , PlayerActivity.class)
                        .putParcelableArrayListExtra("items"
                                , songsList).putExtra("pos", position)});

            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }


}
