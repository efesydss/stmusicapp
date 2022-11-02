package com.st.stmusic.entity;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class PlaylistManager {
    final ArrayList<String> playlistArray = new ArrayList<>();
    final ArrayList<String> songList = new ArrayList<>();
    TinyDB tinyDB;


    Context mContext;
    final int playListSize = 0;

    public void setTinyDB(TinyDB tinyDB) {
        this.tinyDB = tinyDB;
    }

    public ArrayList<String> getPlaylistArray() {
        return playlistArray;
    }


    public ArrayList<String> getSongList() {
        return songList;
    }

    public int getPlayListSize() {
        return playListSize;
    }

    public PlaylistManager() {
    }

    public PlaylistManager(TinyDB tinyDB, Context mContext) {
        this.tinyDB = tinyDB;
        this.mContext = mContext;
    }

    /**
     * PLAYLİST MANAGEMENT
     **/
    public void getPlaylist() {
        playlistArray.clear();
        for (int i = 0; i < tinyDB.getListString("playlist").size(); i++) {
            playlistArray.add(tinyDB.getListString("playlist").get(i));

        }
    }


    public ArrayList<String> getPlaylist(boolean test) {
        playlistArray.clear();
        for (int i = 0; i < tinyDB.getListString("playlist").size(); i++) {
            playlistArray.add(tinyDB.getListString("playlist").get(i));

        }
        return playlistArray;
    }

    public String getPlaylist(String controlKey) {
        playlistArray.clear();
        String situation = "null";
        for (int i = 0; i < tinyDB.getListString("playlist").size(); i++) {
            if (tinyDB.getListString("playlist").get(i).equals(controlKey)) {
                situation = "fail";//çünkü daha önce eklenmiş

            } else if (!situation.equals("fail")) {
                situation = "success";
                playlistArray.add(tinyDB.getListString("playlist").get(i));//ilk defa ekleniyor sorunsuz eklendi.
            }
        }
        return situation;
    }

    public boolean createPlaylist(String playlistName) {
        String ctrl = getPlaylist(playlistName);
        if (ctrl.equals("success") || ctrl.equals("null")) {

            playlistArray.add(playlistName);
            tinyDB.putListString("playlist", playlistArray);
            return true;
        }
        return false;
    }

    public void removePlaylist(String playlistName) {
        //working%50 => ilgili songları silmek kaldı ama önce put etmeyi yap


        String ctrl = getPlaylist(playlistName);

        getPlaylist();
        if (ctrl.equals("success")) {
            //böyle bir playlist yok mesajı ver.
        } else if (ctrl.equals("fail")) {
            Log.e("ctrla", " arraysize " + playlistArray.size() + " \n");
            for (int i = 0; i <= playlistArray.size(); i++) {
                Log.e("ctrla", " playlist song => " + playlistArray.get(i));
                if (playlistName.equals(playlistArray.get(i))) {
                    playlistArray.remove(i);

                    tinyDB.putListString("playlist", playlistArray);
                    Log.e("ctrla", "içeriği değiştirilmiş olan playlistArray db ye put edildi... ");
                    break;


                }
            }
            removeAllSongsThisPlaylist(playlistName);
        }



        /**ilgili songListide silmelisin ...
         * ama önce şarkı put etmelisin tabi
         *
         * **/


    }
    /**PLAYLİST MANAGEMENT**/

    /***************************************************/
    /**
     * PLAYLİST SONG MANAGEMENT
     **/
    public void getPlaylistSongs(String playlistName) {
        songList.clear();
        for (int i = 0; i < tinyDB.getListString(playlistName).size(); i++) {
            songList.add(tinyDB.getListString(playlistName).get(i));
        }
    }

    public void addSongsToPlaylist(String playlistName, String songData) {//ok
        songList.clear();
        getPlaylistSongs(playlistName);

        Log.e("listem ", " " + songList.size());
        songList.add(songData);//songliste gönderilen songData eklendi.
        Log.e("listem", " " + songList.size());
        tinyDB.putListString(playlistName, songList);
        Log.e("listem", "database'e (playlistName) key'i ile songList Arrayi eklendi");

    }

    public void removeAllSongsThisPlaylist(String playlistName){
        tinyDB.remove(playlistName);
    }
    public void removeSongsFromPlaylist(String playlistName, String songData) {
        getPlaylistSongs(playlistName);
        for (int i = 0; i < songList.size(); i++){
            if (songList.get(i).equals(songData)){
                songList.remove(i);
                tinyDB.putListString(playlistName,songList);
                break;
            }
        }
    }

    public int getManySpecificSize(String playlistName){
        songList.clear();
        getPlaylistSongs(playlistName);


        return songList.size();
    }


    /**PLAYLİST SONG MANAGEMENT**/

}
