package com.rubengees.vocables.core.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ruben on 12.05.15.
 */
public class ModeData implements Parcelable {

    public static final Parcelable.Creator<ModeData> CREATOR = new Parcelable.Creator<ModeData>() {

        public ModeData createFromParcel(Parcel in) {
            return new ModeData(in);
        }

        public ModeData[] newArray(int size) {
            return new ModeData[size];
        }

    };
    private int id;
    private int played;
    private int correct;
    private int incorrect;
    private int perfectInRow = 0;
    private int bestTime;
    private int averageTime;

    public ModeData(int id, int played, int correct, int incorrect, int perfectInRow, int bestTime, int averageTime) {
        this.id = id;
        this.played = played;
        this.correct = correct;
        this.incorrect = incorrect;
        this.perfectInRow = perfectInRow;
        this.bestTime = bestTime;
        this.averageTime = averageTime;
    }

    protected ModeData(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(played);
        out.writeInt(correct);
        out.writeInt(incorrect);
        out.writeInt(perfectInRow);
        out.writeInt(bestTime);
        out.writeInt(averageTime);
    }

    private void readFromParcel(Parcel in) {
        played = in.readInt();
        correct = in.readInt();
        incorrect = in.readInt();
        perfectInRow = in.readInt();
        bestTime = in.readInt();
        averageTime = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getPerfectInRow() {
        return perfectInRow;
    }

    public void setPerfectInRow(int perfectInRow) {
        this.perfectInRow = perfectInRow;
    }

    public int getBestTime() {
        return bestTime;
    }

    public void setBestTime(int bestTime) {
        this.bestTime = bestTime;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }
}
