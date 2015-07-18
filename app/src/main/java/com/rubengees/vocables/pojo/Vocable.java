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

        public Vocable createFromParcel(Parcel in) {
            return new Vocable(in);
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

    private Vocable(Parcel in) {
        readFromParcel(in);
    }

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

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        firstMeaningList = in.readParcelable(MeaningList.class.getClassLoader());
        secondMeaningList = in.readParcelable(MeaningList.class.getClassLoader());
        hint = in.readString();
        correct = in.readInt();
        incorrect = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeParcelable(firstMeaningList, flags);
        out.writeParcelable(secondMeaningList, flags);
        out.writeString(hint);
        out.writeInt(correct);
        out.writeInt(incorrect);
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
        if (o != null && o instanceof Vocable) {
            Vocable other = (Vocable) o;

            boolean result = (other == this) || (firstMeaningList.equals(other.getFirstMeaningList()) && secondMeaningList.equals(other.getSecondMeaningList())
                    && (lastModificationTime == other.getLastModificationTime()) &&
                    (getCorrect() == other.getCorrect()) && (incorrect == other.getIncorrect()));
            if (hint != null) {
                result = hint.equals(other.getHint());
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();

        result = 31 * result + this.firstMeaningList.hashCode();
        result = 31 * result + this.secondMeaningList.hashCode();
        result = 31 * result + (this.hint != null ? this.hint.hashCode() : 0);
        result = 31 * result + (int) (this.lastModificationTime ^ lastModificationTime >>> 32);
        result = 31 * result + this.correct;
        result = 31 * result + this.incorrect;

        return result;
    }

    public MeaningList getOtherMeaningList(MeaningList meaningList) {
        if (meaningList.equals(firstMeaningList)) {
            return secondMeaningList;
        } else {
            return firstMeaningList;
        }
    }
}