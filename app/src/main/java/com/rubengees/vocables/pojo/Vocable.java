package com.rubengees.vocables.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Vocable implements TrainerItem, Parcelable {

    public static final Parcelable.Creator<Vocable> CREATOR = new Parcelable.Creator<Vocable>() {
        public Vocable createFromParcel(Parcel source) {
            return new Vocable(source);
        }

        public Vocable[] newArray(int size) {
            return new Vocable[size];
        }
    };
    private Integer id;
    private MeaningList firstMeaningList;
    private MeaningList secondMeaningList;
    private String hint;
    private long lastModificationTime;
    private int correct;
    private int incorrect;

    public Vocable(@NonNull MeaningList firstMeaningList, @NonNull MeaningList secondMeaningList, @Nullable String hint, long lastModificationTime) {
        this.firstMeaningList = firstMeaningList;
        this.secondMeaningList = secondMeaningList;
        this.hint = hint;
        this.lastModificationTime = lastModificationTime;
        this.correct = 0;
        this.incorrect = 0;
    }

    public Vocable(@NonNull Integer id, @NonNull MeaningList firstMeaningList, @NonNull MeaningList secondMeaningList, int correct, int incorrect, @Nullable String hint, long lastModificationTime) {
        this.id = id;
        this.firstMeaningList = firstMeaningList;
        this.secondMeaningList = secondMeaningList;
        this.hint = hint;
        this.lastModificationTime = lastModificationTime;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    protected Vocable(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstMeaningList = in.readParcelable(MeaningList.class.getClassLoader());
        this.secondMeaningList = in.readParcelable(MeaningList.class.getClassLoader());
        this.hint = in.readString();
        this.lastModificationTime = in.readLong();
        this.correct = in.readInt();
        this.incorrect = in.readInt();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCorrect() {
        return correct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIncorrect() {
        return incorrect;
    }

    public MeaningList getFirstMeaningList() {
        return firstMeaningList;
    }

    public void setFirstMeaningList(MeaningList firstMeaningList) {
        this.firstMeaningList = firstMeaningList;
    }

    public MeaningList getSecondMeaningList() {
        return secondMeaningList;
    }

    public void setSecondMeaningList(MeaningList secondMeaningList) {
        this.secondMeaningList = secondMeaningList;
    }

    public void processAnswer(boolean correct) {
        if (correct) {
            this.correct++;
        } else {
            this.incorrect++;
        }
    }

    public String getHint() {
        return hint;
    }

    public void setHint(@Nullable String hint) {
        this.hint = hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vocable vocable = (Vocable) o;

        if (lastModificationTime != vocable.lastModificationTime) return false;
        if (correct != vocable.correct) return false;
        if (incorrect != vocable.incorrect) return false;
        if (id != null ? !id.equals(vocable.id) : vocable.id != null) return false;
        if (!firstMeaningList.equals(vocable.firstMeaningList)) return false;
        if (!secondMeaningList.equals(vocable.secondMeaningList)) return false;
        return !(hint != null ? !hint.equals(vocable.hint) : vocable.hint != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + firstMeaningList.hashCode();
        result = 31 * result + secondMeaningList.hashCode();
        result = 31 * result + (hint != null ? hint.hashCode() : 0);
        result = 31 * result + (int) (lastModificationTime ^ (lastModificationTime >>> 32));
        result = 31 * result + correct;
        result = 31 * result + incorrect;
        return result;
    }

    public MeaningList getOtherMeaningList(MeaningList meaningList) {
        if (meaningList.equals(firstMeaningList)) {
            return secondMeaningList;
        } else {
            return firstMeaningList;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeParcelable(this.firstMeaningList, 0);
        dest.writeParcelable(this.secondMeaningList, 0);
        dest.writeString(this.hint);
        dest.writeLong(this.lastModificationTime);
        dest.writeInt(this.correct);
        dest.writeInt(this.incorrect);
    }
}