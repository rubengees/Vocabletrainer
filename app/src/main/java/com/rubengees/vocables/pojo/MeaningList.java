package com.rubengees.vocables.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A pojo which contains Meanings in one language.
 */
public class MeaningList implements Comparable<MeaningList>, Iterable<String>, Parcelable {

    public static final Parcelable.Creator<MeaningList> CREATOR = new Parcelable.Creator<MeaningList>() {

        public MeaningList createFromParcel(Parcel in) {
            return new MeaningList(in);
        }

        public MeaningList[] newArray(int size) {
            return new MeaningList[size];
        }

    };

    private List<String> meanings;

    public MeaningList(@NonNull List<String> meanings) {
        if (meanings.isEmpty()) {
            throw new RuntimeException("The list with the meanings cannot be empty");
        }

        this.meanings = new LinkedList<>();
        setMeanings(meanings);
    }

    private MeaningList(Parcel in) {
        readFromParcel(in);
    }

    public MeaningList(@NonNull String given) {
        this.meanings = new LinkedList<>();
        this.meanings.add(given);
    }

    /**
     * Returns the Meanings of this MeaningList as an List of Strings.
     *
     * @return The List of Meanings
     */
    public List<String> getMeanings() {
        return new LinkedList<>(meanings);
    }

    /**
     * Sets the different Meanings of this MeaningList.
     *
     * @param meanings The new Meanings
     */
    public void setMeanings(@NonNull Collection<String> meanings) {
        if (meanings.isEmpty()) {
            throw new RuntimeException("The list with the meanings cannot be empty");
        }

        this.meanings.clear();
        this.meanings.addAll(meanings);
    }

    /**
     * Checks if a single Meaning is in this MeaningList.
     *
     * @param meaning A meaning
     * @return True if a the meaning is in this list, false otherwise
     */
    public boolean contains(String meaning) {
        return meanings.contains(meaning);
    }

    /**
     * Checks if a single Meaning is in this MeaningList. The case of the Meaning is ignored.
     *
     * @param meaning A meaning
     * @return True
     */
    public boolean containsIgnoreCase(String meaning) {
        for (String currentMeaning : meanings) {
            if (currentMeaning.equalsIgnoreCase(meaning)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a Object equals to this Meaning.
     *
     * @param another An other Object
     * @return True if this Meaning equals the other Object. More about the criteria in the Apache
     * JavaDoc
     */
    @Override
    public boolean equals(Object another) {
        if (another == this) {
            return true;
        }

        if (another instanceof MeaningList) {
            List<String> copy = new ArrayList<>(meanings);

            for (String s : ((MeaningList) another).getMeanings()) {
                if (!copy.remove(s)) {
                    return false;
                }
            }

            return copy.size() <= 0;
        } else {
            return false;
        }

    }

    /**
     * Returns a String-representation of this Meaning in the following scheme:
     * first/second/third/fourth
     *
     * @return A string, representing this Meaning
     */
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

    /**
     * Return if this Meaning equals another Object. Case of the Meanings is ignored.
     *
     * @param another An other Meaning
     * @return True if this Meaning is equal to ther other Object
     */
    public boolean equalsIgnoreCase(Object another) {
        if (another == this) {
            return true;
        }

        if (another instanceof MeaningList) {
            List<String> copy = new LinkedList<>(meanings);

            for (String s : ((MeaningList) another).getMeanings()) {
                ListIterator<String> iterator = copy.listIterator();

                while (iterator.hasNext()) {
                    if (iterator.next().equalsIgnoreCase(s)) {
                        iterator.remove();
                    }
                }
            }

            return copy.size() == 0;
        } else {
            return false;
        }
    }

    private void readFromParcel(Parcel in) {
        this.meanings = new LinkedList<>();

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

    /**
     * Compares this MeaningList to another MeaningList. If this is Lexicographically before the other one,
     * an int >= 1 is returned, an int <= -1 is returned. If both Objects are equal, 0 is returned.
     *
     * @param meaningList Another MeaningList tho compare this one to
     * @return An int representing the relation of this item to the other
     */
    @Override
    public int compareTo(@NonNull MeaningList meaningList) {
        return meanings.toString().compareToIgnoreCase(meaningList.getMeanings().toString());
    }

    @Override
    public Iterator<String> iterator() {
        return new LinkedList<>(meanings).iterator();
    }

    public List<String> toList() {
        return new LinkedList<>(meanings);
    }
}
