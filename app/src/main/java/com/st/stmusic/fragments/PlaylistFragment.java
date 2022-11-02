package com.st.stmusic.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.st.stmusic.R;
import com.st.stmusic.adapter.PlaylistAdapter;
import com.st.stmusic.data.MusicModel;
import com.st.stmusic.databinding.FragmentPlaylistBinding;
import com.st.stmusic.entity.PlaylistManager;
import com.st.stmusic.entity.TinyDB;

import java.util.ArrayList;


public class PlaylistFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentPlaylistBinding binding;
    ArrayList<MusicModel> audioList;
    final ArrayList<String> playlistArray = new ArrayList<>();
    final ArrayList<String> songList = new ArrayList<>();
    TinyDB tinyDB;
    PlaylistAdapter adapter;
    PlaylistManager manager;
    final int playListSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);

        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(requireContext()));


        tinyDB = new TinyDB(requireContext());


        manager = new PlaylistManager(tinyDB, requireContext());

        manager.getPlaylist();


        adapter = new PlaylistAdapter(requireContext(), manager.getPlaylistArray(), manager.getPlayListSize(), tinyDB);
        binding.rvPlaylist.setAdapter(adapter);
        binding.rvPlaylist.setHasFixedSize(true);


        return binding.getRoot();


    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    public void menu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.toolbar_playlist_menu, menu);
                MenuItem item = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(PlaylistFragment.this);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_create_playlist:

                        View alertView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_create_playlist, null);

                        EditText txtName = alertView.findViewById(R.id.txtPlaylistNameEdit);

                        TextView txtNamePlaylist = alertView.findViewById(R.id.txtPlaylistName);
                        Button btnOk = alertView.findViewById(R.id.btnCreate);


                        final Dialog ad = new Dialog(requireContext(), R.style.MY_AlertDialog);


                        ad.setContentView(alertView);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!txtName.getText().toString().trim().equals("")) {
                                    boolean sit = manager.createPlaylist(txtName.getText().toString().trim());
                                    if (sit) {
                                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                    ad.dismiss();//dialog dismiss
                                } else {
                                    Toast.makeText(requireContext(), "Please Enter a Name", Toast.LENGTH_SHORT).show();
                                    txtNamePlaylist.setText("Please Enter a Name");
                                }
                            }
                        });


                        txtName.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                txtNamePlaylist.setText(s);
                                if (count == 0) {
                                    txtNamePlaylist.setText("Please Enter a Name");
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {


                            }
                        });
                        ad.create();
                        ad.show();
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onResume() {

        getActivity().invalidateOptionsMenu();
        menu();
        adapter.notifyDataSetChanged();
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