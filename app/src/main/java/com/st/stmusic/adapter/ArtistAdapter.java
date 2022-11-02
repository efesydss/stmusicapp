package com.st.stmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.st.stmusic.R;
import com.st.stmusic.data.ArtistModel;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.ArtistCardBinding;
import com.st.stmusic.databinding.PlaylistCardBinding;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private final Context mContext;
    private final List<ArtistModel> modelList;

    public ArtistAdapter(Context mContext, List<ArtistModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        ArtistCardBinding itemView;

        public ArtistViewHolder(@NonNull ArtistCardBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ArtistCardBinding binding = ArtistCardBinding.inflate(layoutInflater, parent, false);
        return new ArtistAdapter.ArtistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {

        ArtistModel model = modelList.get(position);
        ArtistCardBinding t = holder.itemView;
        t.txtName.setText(model.getArtistName());
        t.txtCount.setText(String.valueOf(modelList.size()));
        Picasso.get()
                .load(model.getImageUri())
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(t.imgSongImage);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
