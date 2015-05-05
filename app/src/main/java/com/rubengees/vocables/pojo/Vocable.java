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
    private Meaning firstMeaning;
    private Meaning secondMeaning;
    private String hint;
    private long lastModificationTime;
    private int correct;
    private int incorrect;

    private Vocable(Parcel in) {
        readFromParcel(in);
    }

    public Vocable(@NonNull Meaning firstMeaning, @NonNull Meaning secondMeaning, @Nullable String hint, long lastModificationTime) {
        this.firstMeaning = firstMeaning;
        this.secondMeaning = secondMeaning;
        this.hint = hint;
        this.lastModificationTime = lastModificationTime;
        this.correct = 0;
        this.incorrect = 0;
    }

    public Vocable(@NonNull Integer id, @NonNull Meaning firstMeaning, @NonNull Meaning secondMeaning, int correct, int incorrect, @Nullable String hint, long lastModificationTime) {
        this.id = id;
        this.firstMeaning = firstMeaning;
        this.secondMeaning = secondMeaning;
        this.hint = hint;
        this.lastModificationTime = lastModificationTime;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        firstMeaning = in.readParcelable(firstMeaning.getClass().getClassLoader());
        secondMeaning = in.readParcelable(secondMeaning.getClass().getClassLoader());
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
        out.writeParcelable(firstMeaning, 0);
        out.writeParcelable(secondMeaning, 0);
        out.writeString(hint);
        out.writeInt(correct);
        out.writeInt(incorrect);
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

    @Override
    public int getCorrect() {
        return correct;
    }

    @Override
    public int getIncorrect() {
        return incorrect;
    }

    public Meaning getFirstMeaning() {
        return firstMeaning;
    }

    public void setFirstMeaning(Meaning firstMeaning) {
        this.firstMeaning = firstMeaning;
    }

    public Meaning getSecondMeaning() {
        return secondMeaning;
    }

    public void setSecondMeaning(Meaning secondMeaning) {
        this.secondMeaning = secondMeaning;
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
}
