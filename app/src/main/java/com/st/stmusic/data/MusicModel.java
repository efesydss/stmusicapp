package com.st.stmusic.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicModel implements Parcelable {
    private String ID;
    private String mData;
    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private String duration;
    private Uri uri;
    private String position;
    private int isLiked;

    public MusicModel(String ID, String mData, String mTitle, String mAlbum, String mArtist, Uri songImage, String duration, Uri uri, String position, int isLiked) {
        this.ID = ID;
        this.mData = mData;
        this.mTitle = mTitle;
        this.mAlbum = mAlbum;
        this.mArtist = mArtist;
        this.songImage = songImage;
        this.duration = duration;
        this.uri = uri;
        this.position = position;
        this.isLiked = isLiked;
    }

    protected MusicModel(Parcel in) {
        ID = in.readString();
        mData = in.readString();
        mTitle = in.readString();
        mAlbum = in.readString();
        mArtist = in.readString();
        duration = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        songImage = in.readParcelable(Uri.class.getClassLoader());
        position = in.readString();
        isLiked = in.readInt();


    }

    public static final Creator<MusicModel> CREATOR = new Creator<MusicModel>() {
        @Override
        public MusicModel createFromParcel(Parcel in) {
            return new MusicModel(in);
        }

        @Override
        public MusicModel[] newArray(int size) {
            return new MusicModel[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Uri getSongImage() {
        return songImage;
    }

    public void setSongImage(Uri songImage) {
        this.songImage = songImage;
    }

    private Uri songImage;

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(mData);
        dest.writeString(mTitle);
        dest.writeString(mAlbum);
        dest.writeString(mArtist);
        dest.writeString(duration);
        dest.writeParcelable(uri, flags);
        dest.writeParcelable(songImage, flags);
        dest.writeString(position);
        dest.writeInt(isLiked);
    }
}
