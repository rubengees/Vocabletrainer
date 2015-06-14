package com.rubengees.vocables.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Meaning implements Comparable<Meaning>, Iterable<String>, Parcelable {

    public static final Parcelable.Creator<Meaning> CREATOR = new Parcelable.Creator<Meaning>() {

        public Meaning createFromParcel(Parcel in) {
            return new Meaning(in);
        }

        public Meaning[] newArray(int size) {
            return new Meaning[size];
        }

    };
    private List<String> meanings;

    public Meaning(List<String> meanings) {
        if (meanings.isEmpty()) {
            throw new RuntimeException("The list with the meanings cannot be empty");
        }
        this.meanings = meanings;
    }

    private Meaning(Parcel in) {
        readFromParcel(in);
    }

    public Meaning(@NonNull String given) {
        this.meanings = new ArrayList<>();
        meanings.add(given);
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> firstMeanings) {
        this.meanings = firstMeanings;
    }

    public boolean contains(String s) {
        return meanings.contains(s);
    }

    public boolean containsIgnoreCase(Object o) {
        if (o instanceof String) {
            for (String meaning : meanings) {
                if (meaning.equalsIgnoreCase((String) o)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object another) {
        if (another == this) {
            return true;
        }

        if (another instanceof Meaning) {
            List<String> copy = new ArrayList<>(meanings);

            for (String s : ((Meaning) another).getMeanings()) {
                if (!copy.remove(s)) {
                    return false;
                }
            }

            return copy.size() <= 0;
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        String result = "";

        if (meanings.size() > 0) {
            result += meanings.get(0);
        }

        for (int i = 1; i < meanings.size(); i++) {
            result += "/";
            result += meanings.get(i);
        }

        return result;
    }

    public boolean equalsIgnoreCase(Object another) {
        if (another == this) {
            return true;
        }

        if (another instanceof Meaning) {
            List<String> copy = new ArrayList<>(meanings);

            for (String s : ((Meaning) another).getMeanings()) {
                for (int i = 0; i < copy.size(); i++) {
                    if (copy.get(i).equalsIgnoreCase(s)) {
                        copy.remove(i);
                    }
                }
            }

            return copy.size() == 0;
        } else {
            return false;
        }
    }

    private void readFromParcel(Parcel in) {
        in.readStringList(meanings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringList(meanings);
    }

    @Override
    public int compareTo(@NonNull Meaning meaning) {
        return meanings.toString().compareToIgnoreCase(meaning.getMeanings().toString());
    }

    @Override
    public Iterator<String> iterator() {
        return meanings.iterator();
    }

    public List<String> toList() {
        return new ArrayList<String>(meanings);
    }
}
