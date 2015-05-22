package com.rubengees.vocables.utils;

import java.io.File;
import java.util.Comparator;

/**
 * Created by ruben on 22.05.15.
 */
public class FileComparator implements Comparator<File> {

    @Override
    public int compare(File lhs, File rhs) {
        if (lhs.isFile() && !rhs.isFile()) {
            return 1;
        } else if (lhs.isDirectory() && !rhs.isDirectory()) {
            return -1;
        } else {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    }

}
