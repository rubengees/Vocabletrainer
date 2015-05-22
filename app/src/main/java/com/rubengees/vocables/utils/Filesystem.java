package com.rubengees.vocables.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Filesystem implements Parcelable {


    public static final Parcelable.Creator<Filesystem> CREATOR = new Parcelable.Creator<Filesystem>() {

        public Filesystem createFromParcel(Parcel in) {
            return new Filesystem(in);
        }

        public Filesystem[] newArray(int size) {
            return new Filesystem[size];
        }

    };
    private String root;
    private List<String> subDirs;

    private Filesystem(Parcel in) {
        readFromParcel(in);
    }

    public Filesystem(String pRoot) {
        root = pRoot;
        subDirs = new ArrayList<>();
    }

    private void readFromParcel(Parcel in) {
        root = in.readString();
        in.readStringList(subDirs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(root);
        out.writeStringList(subDirs);
    }

    public String getPath() {
        String path = root;
        for (int i = 0; i < subDirs.size(); i++)
            path += "/" + subDirs.get(i);

        return path;
    }

    public boolean cdUp() {
        if (subDirs.size() == 0) return false;
        else subDirs.remove(subDirs.size() - 1);
        return true;
    }

    public boolean cd(String subDirectory) {
        File f = new File(getPath() + "/" + subDirectory);
        if (!f.exists()) {
            return false;
        } else {
            subDirs.add(subDirectory);
            return true;
        }
    }

    public boolean isRoot() {
        return subDirs.isEmpty();
    }

    public List<File> getFiles() {
        List<File> result = new ArrayList<>();
        String path = getPath();
        File f = new File(path);
        String[] list = f.list();

        for (String name : list) {
            result.add(new File(path + "/" + name));
        }

        return result;
    }
}