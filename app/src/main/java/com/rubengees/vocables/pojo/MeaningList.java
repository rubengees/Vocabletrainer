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
public class MeaningList implements List<String>, Comparable<MeaningList>, Parcelable {

    public static final Parcelable.Creator<MeaningList> CREATOR = new Parcelable.Creator<MeaningList>() {
        public MeaningList createFromParcel(Parcel source) {
            return new MeaningList(source);
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

    public MeaningList(@NonNull String given) {
        this.meanings = new LinkedList<>();
        this.meanings.add(given);
    }

    protected MeaningList(Parcel in) {
        this.meanings = in.createStringArrayList();
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

    @Override
    public void add(int location, String object) {
        meanings.add(location, object);
    }

    @Override
    public boolean add(String object) {
        return meanings.add(object);
    }

    @Override
    public boolean addAll(int location, @NonNull Collection<? extends String> collection) {
        return meanings.addAll(location, collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends String> collection) {
        return meanings.addAll(collection);
    }

    @Override
    public void clear() {
        meanings.clear();
    }

    /**
     * Checks if a single Meaning is in this MeaningList.
     *
     * @param object A Object
     * @return True if a the meaning is in this list, false otherwise
     */
    @Override
    public boolean contains(Object object) {
        return meanings.contains(object);
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

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return meanings.containsAll(collection);
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

    @Override
    public int hashCode() {
        return meanings.hashCode();
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

    @Override
    public String get(int location) {
        return meanings.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return meanings.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return meanings.isEmpty();
    }

    /**
     * Compares this MeaningList to another MeaningList. If this is Lexicographically (not case-sensitive) before the other one,
     * an int >= 1 is returned, otherwise an int <= -1 is returned. If both Objects are equal, 0 is returned.
     *
     * @param meaningList Another MeaningList tho compare this one to
     * @return An int representing the relation of this item to the other
     */
    @Override
    public int compareTo(@NonNull MeaningList meaningList) {
        return meanings.toString().compareToIgnoreCase(meaningList.getMeanings().toString());
    }

    @NonNull
    @Override
    public Iterator<String> iterator() {
        return new LinkedList<>(meanings).iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<String> listIterator() {
        return new LinkedList<>(meanings).listIterator();
    }

    @NonNull
    @Override
    public ListIterator<String> listIterator(int location) {
        return new LinkedList<>(meanings).listIterator(location);
    }

    @Override
    public String remove(int location) {
        if (meanings.size() <= 1) {
            throw new RuntimeException("Cannot remove the last meaning.");
        } else {
            return meanings.remove(location);
        }
    }

    @Override
    public boolean remove(Object object) {
        if (meanings.size() <= 1) {
            throw new RuntimeException("Cannot remove the last meaning.");
        } else {
            return meanings.remove(object);
        }
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        if (meanings.size() - collection.size() <= 0) {
            throw new RuntimeException("Cannot remove the last meaning.");
        } else {
            return false;
        }
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        boolean result = meanings.retainAll(collection);

        if (meanings.isEmpty()) {
            throw new RuntimeException("Cannot remove the last meaning.");
        } else {
            return result;
        }
    }

    @Override
    public String set(int location, String object) {
        return meanings.set(location, object);
    }

    @Override
    public int size() {
        return meanings.size();
    }

    @NonNull
    @Override
    public List<String> subList(int start, int end) {
        return meanings.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return meanings.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] array) {
        //noinspection SuspiciousToArrayCall
        return meanings.toArray(array);
    }

    public List<String> toList() {
        return new LinkedList<>(meanings);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.meanings);
    }
}
