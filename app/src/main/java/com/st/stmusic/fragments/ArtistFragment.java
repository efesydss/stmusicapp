package com.st.stmusic.fragments;

import static android.provider.MediaStore.MediaColumns.TITLE;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import com.st.stmusic.adapter.ArtistAdapter;
import com.st.stmusic.data.ArtistModel;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.FragmentArtistBinding;

import java.util.ArrayList;


public class ArtistFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentArtistBinding binding;

    ArrayList<ArtistModel> model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistBinding.inflate(inflater, container, false);
        binding.rvArtist.setLayoutManager(new LinearLayoutManager(requireContext()));

        menu();
        loadData();



        return binding.getRoot();
    }

    public void loadData() {
        ContentResolver contentResolver = requireContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        model = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                @SuppressLint("Range") String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") String dataForImage = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                model.add(new ArtistModel(artist,1,Uri.parse(dataForImage)));
            }
        }/**audioList adaptere yolla ve finish**/
        cursor.close();


        ArtistAdapter adapter = new ArtistAdapter(requireContext(), model);
        binding.rvArtist.setAdapter(adapter);
        binding.rvArtist.setHasFixedSize(true);
    }

    public void menu(){
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.toolbar_artist_menu, menu);
                MenuItem item = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(ArtistFragment.this);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
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