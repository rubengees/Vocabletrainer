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
public class Unit implements TrainerItem, Parcelable, Iterable<Vocable>, Comparable<Unit> {

    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {

        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        public Unit[] newArray(int size) {
            return new Unit[size];
        }

    };
    private Integer id;
    private String title;
    private List<Vocable> vocables;
    private long lastModificationTime;

    private Unit(Parcel in) {
        readFromParcel(in);
    }

    public Unit(@NonNull Integer id, String title, long lastModificationTime) {
        this();
        this.id = id;
        this.title = title;
        this.lastModificationTime = lastModificationTime;
    }

    public Unit() {
        vocables = new ArrayList<>();
    }

    @Override
    public Long getLastModificationTime() {
        return lastModificationTime;
    }

    @Override
    public void setLastModificationTime(long time) {
        this.lastModificationTime = time;
    }

    @Override
    public Integer getId() {
        return id;
    }

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

    @Override
    public int getCorrect() {
        int result = 0;

        for (Vocable vocable : vocables) {
            result += vocable.getCorrect();
        }

        return result;
    }

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

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        lastModificationTime = in.readLong();
        in.readTypedList(vocables, Vocable.CREATOR);
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeLong(lastModificationTime);
        out.writeTypedList(vocables);
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
}
