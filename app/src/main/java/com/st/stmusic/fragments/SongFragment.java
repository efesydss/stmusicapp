package com.st.stmusic.fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.st.stmusic.R;
import com.st.stmusic.adapter.SongAdapter;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.FragmentSongBinding;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;


public class SongFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentSongBinding binding;
    ArrayList<MusicModel> audioList;
    TinyDB tinydb;
    SongAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSongBinding.inflate(inflater, container, false);
        binding.rvSong.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadData();

        menu();
        tinydb = new TinyDB(requireContext());


        return binding.getRoot();
    }

    public void menu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.toolbar_songs_menu, menu);
                MenuItem item = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(SongFragment.this);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return true;
            }
        });
    }

    public void loadData() {
        ContentResolver contentResolver = requireContext().getContentResolver();


        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        audioList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {


            while (cursor.moveToNext()) {

                @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                @SuppressLint("Range") String genre = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.GENRE));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                @SuppressLint("Range") String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));//biraz daha uzun şarkı adı gibi daha detay var title sadece ad soyad gibi düşün..
                @SuppressLint("Range") String volume = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.VOLUME_NAME));//sd card mı yoksa ana diskte mi onu söylüyor.
                @SuppressLint("Range") String bucketName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME));//içerisinde bulunduğu klasör
                Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                /**
                 * duration= 24.600
                 * duration / 60.000  = > dakikayı verir
                 * (duration % 60.000) / 1000  = > dakikadan artan saniyeyi verir
                 * **/
                String minutes = String.valueOf(Integer.valueOf(duration) / 60000);
                String seconds = String.valueOf((Integer.valueOf(duration) % 60000) / 1000);
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                // Save to audioList


                audioList.add(new MusicModel(ID, data, title, album, artist, albumArtUri, duration, uri, "0", 0));


                Log.e("test",
                        "\n data = " + data +
                                "\n" + "title = " + title +
                                "\n" + "album = " + album +
                                "\n" + "artist = " + artist +

                                "\n" + "display name = " + displayName +
                                "\n" + "volume name = " + volume +
                                "\n" + "bucket name = " + bucketName +
                                "\n" + "ID = " + ID);

                Log.e("myid", ID);


            }
        }/**audioList adaptere yolla ve finish**/
        cursor.close();





        //adapter size 0 olmamalı
            adapter = new SongAdapter(requireContext(), audioList, tinydb);
            binding.rvSong.setAdapter(adapter);
            binding.rvSong.setHasFixedSize(true);




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {

        getActivity().invalidateOptionsMenu();


        menu();
        super.onResume();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}