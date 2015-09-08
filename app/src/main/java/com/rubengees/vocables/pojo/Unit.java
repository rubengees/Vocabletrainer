package com.rubengees.vocables.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.rubengees.vocables.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Unit implements TrainerItem, Iterable<Vocable>, Comparable<Unit>, Parcelable {
    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {
        public Unit createFromParcel(Parcel source) {
            return new Unit(source);
        }

        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };
    private Integer id;
    private String title;
    private List<Vocable> vocables;
    private long lastModificationTime;

    public Unit(@NonNull Integer id, String title, long lastModificationTime) {
        this();
        this.id = id;
        this.title = title;
        this.lastModificationTime = lastModificationTime;
    }

    public Unit() {
        vocables = new ArrayList<>();
    }

    protected Unit(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.vocables = in.createTypedArrayList(Vocable.CREATOR);
        this.lastModificationTime = in.readLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLastModificationTime() {
        return lastModificationTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateModificationTime() {
        this.lastModificationTime = System.currentTimeMillis();
    }

    public void setModificationTime(long modificationTime) {
        this.lastModificationTime = modificationTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCorrect() {
        int result = 0;

        for (Vocable vocable : vocables) {
            result += vocable.getCorrect();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIncorrect() {
        int result = 0;

        for (Vocable vocable : vocables) {
            result += vocable.getIncorrect();
        }

        return result;
    }

    public List<Vocable> getVocables() {
        return new ArrayList<>(vocables);
    }

    public List<Vocable> getVocables(int maxRate) {
        ArrayList<Vocable> result = new ArrayList<>();

        for (Vocable vocable : vocables) {
            if (Utils.calculateCorrectAnswerRate(vocable.getCorrect(), vocable.getIncorrect()) > maxRate) {
                continue;
            }

            result.add(vocable);
        }

        return result;
    }

    public int size() {
        return vocables.size();
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public void add(Vocable vocable) {
        vocables.add(vocable);
    }

    public void addAll(Collection<Vocable> vocablesToAdd) {
        vocables.addAll(vocablesToAdd);
    }

    public void clear() {
        vocables.clear();
    }

    public void remove(int pos) {
        vocables.remove(pos);
    }

    public boolean remove(Vocable vocable) {
        return vocables.remove(vocable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit vocables = (Unit) o;

        return lastModificationTime == vocables.lastModificationTime && id.equals(vocables.id) && title.equals(vocables.title);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();

        result = 31 * result + title.hashCode();
        result = 31 * result + (int) (lastModificationTime ^ (lastModificationTime >>> 32));

        return result;
    }

    @Override
    public Iterator<Vocable> iterator() {
        return vocables.iterator();
    }

    @Override
    public int compareTo(@NonNull Unit another) {
        return title.compareTo(another.getTitle());
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeTypedList(vocables);
        dest.writeLong(this.lastModificationTime);
    }
}