package com.rubengees.vocables.utils;

import java.io.File;
import java.util.ArrayList;


public class Filesystem {

    private final String root;
    private final ArrayList<String> subDirs;

    public Filesystem(String pRoot) {
        root = pRoot;
        subDirs = new ArrayList<>();
    }

    public String getPath() {
        String path = root;
        for (int i = 0; i < subDirs.size(); i++)
            path += "/" + subDirs.get(i);

        return path;
    }

    public ArrayList<File> getList() {
        ArrayList<File> result = new ArrayList<>();
        String path = getPath();
        File f = new File(path);

        String[] list = f.list();
        for (String name : list) {
            result.add(new File(path + "/" + name));
        }
        return result;
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
}