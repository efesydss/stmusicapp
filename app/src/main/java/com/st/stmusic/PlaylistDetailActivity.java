package com.st.stmusic;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;
import com.st.stmusic.adapter.SongAdapter;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.ActivityPlaylistDetailBinding;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;

public class PlaylistDetailActivity extends AppCompatActivity {
    public ActivityPlaylistDetailBinding binding;
    ArrayList<MusicModel> audioList;
    SongAdapter adapter;
    TinyDB tinydb;
    String currentPlaylist;
    ArrayList<String> takenPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playlist_detail);
        setSupportActionBar(binding.toolbarPlaylist3);
        binding.toolbarPlaylist3.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);//çalışıyor sadece siyah temasal yanee:)


        binding.rvPlaylistSong.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        currentPlaylist = getIntent().getExtras().getString("currentPlaylist");


        binding.toolbarPlaylist3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tinydb = new TinyDB(getApplicationContext());
        takenPlaylist = new ArrayList<>();
        takenPlaylist = tinydb.getListString(currentPlaylist);


        loadData();
        this.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.player_activity_toolbar_menu, menu);//fix it
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void loadData( ) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Log.e("uriproblems", "1=> " + uri + selection);

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        //https://emrealtunbilek.com/android/25-content-resolver-kullanimi-android-persistent-data-ve-contentprovider/
        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();

            while (cursor.moveToNext()) {

                @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                @SuppressLint("Range") String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);




                for (int i = 0; i < takenPlaylist.size(); i++){
                    if (takenPlaylist.get(i).equals(data)){
                        audioList.add(new MusicModel(ID, data, title, album, artist, albumArtUri, duration, uri, "0", 0));
                    }

                }


            }
        }/**audioList adaptere yolla ve finish**/
        cursor.close();


        if (audioList.size()>0){
            Picasso.get()
                    .load(audioList.get(0).getSongImage())
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(binding.imgPlaylistCoverDetails);
        }

        adapter = new SongAdapter(getApplicationContext(), audioList, tinydb);

        //  binding.rvPlaylistSong.setHasFixedSize(true);
        binding.rvPlaylistSong.setAdapter(adapter);

    }
}