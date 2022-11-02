package com.st.stmusic.data;

import android.net.Uri;

import java.io.Serializable;

public class ArtistModel implements Serializable {
    private String artistName;
    private int artistID;
    private Uri imageUri;

    public ArtistModel(String artistName, int artistID,Uri imageUri) {
        this.artistName = artistName;
        this.artistID = artistID;
        this.imageUri=imageUri;

    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }
}
