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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.st.stmusic.R;
import com.st.stmusic.adapter.FavoritesAdapter;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.FragmentFavoritesBinding;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment  {
    FragmentFavoritesBinding binding;
    static ArrayList<MusicModel> audioList;
    TinyDB tinydb;
    ArrayList<String> gelenFavorited;
    FavoritesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        tinydb = new TinyDB(requireContext());

        Toast.makeText(requireContext(), "Favorite created", Toast.LENGTH_SHORT).show();

        loadData();
       // menu();




        return binding.getRoot();
    }


    public void loadData() {
        ContentResolver contentResolver = requireContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";


        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        audioList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            gelenFavorited = new ArrayList<>();
            for (int i = 0; i < tinydb.getListString("favorites").size(); i++) {
                gelenFavorited.add(tinydb.getListString("favorites").get(i));
            }


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

                Log.e("ege", "raw duration = " + duration);
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


                for (int i = 0; i < gelenFavorited.size(); i++) {
                    if (gelenFavorited.get(i).equals(data)) {
                        audioList.add(new MusicModel(ID, data, title, album, artist, albumArtUri, duration, uri, "0", 0));
                    }
                }


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


        adapter = new FavoritesAdapter(requireContext(), audioList);
        binding.rvFavorites.setAdapter(adapter);
        binding.rvFavorites.setHasFixedSize(true);
    }


    @Override
    public void onResume() {

       // getActivity().invalidateOptionsMenu();
       // menu();
        loadData();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





}