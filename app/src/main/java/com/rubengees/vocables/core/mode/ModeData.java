package com.rubengees.vocables.core.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A pojo containing the data of a {@link Mode}.
 */
public class ModeData implements Parcelable {

    public static final Parcelable.Creator<ModeData> CREATOR = new Parcelable.Creator<ModeData>() {
        public ModeData createFromParcel(Parcel source) {
            return new ModeData(source);
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
        this.id = in.readInt();
        this.played = in.readInt();
        this.correct = in.readInt();
        this.incorrect = in.readInt();
        this.perfectInRow = in.readInt();
        this.bestTime = in.readInt();
        this.averageTime = in.readInt();
    }

    /**
     * Returns the ID.
     *
     * @return The ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id The ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the amount of played Rounds.
     *
     * @return The amount
     */
    public int getPlayed() {
        return played;
    }

    /**
     * Sets the amount of played Rounds.
     *
     * @param played The amount
     */
    public void setPlayed(int played) {
        this.played = played;
    }

    /**
     * Returns the amount of correct Answers given while playing this Mode.
     *
     * @return The amount
     */
    public int getCorrect() {
        return correct;
    }

    /**
     * Sets the amount of correct Answers given while playing this Mode.
     *
     * @param correct The amount
     */
    public void setCorrect(int correct) {
        this.correct = correct;
    }

    /**
     * Returns the amount of incorrect Answers given while playing this Mode.
     *
     * @return The amount
     */
    public int getIncorrect() {
        return incorrect;
    }

    /**
     * Sets the amount of incorrect Answers given while playing this Mode.
     *
     * @param incorrect The amount
     */
    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    /**
     * Returns the amount of perfect games in a row.
     *
     * @return The amount
     */
    public int getPerfectInRow() {
        return perfectInRow;
    }

    /**
     * Sets the amount of perfect games in a row
     *
     * @param perfectInRow The amount
     */
    public void setPerfectInRow(int perfectInRow) {
        this.perfectInRow = perfectInRow;
    }

    /**
     * Returns the best time the mode was ever completed in per Vocable.
     *
     * @return The time
     */
    public int getBestTime() {
        return bestTime;
    }

    /**
     * Sets the best time the mode was ever completed in per Vocable.
     *
     * @param bestTime The time
     */
    public void setBestTime(int bestTime) {
        this.bestTime = bestTime;
    }

    /**
     * Returns the average time the mode was completed in per Vocable.
     *
     * @return The time
     */
    public int getAverageTime() {
        return averageTime;
    }

    /**
     * Sets the average time the mode was completed in per Vocable.
     *
     * @param averageTime The time
     */
    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    /**
     * Resets the data of this mode. The perfectOnRow field is not affected.
     */
    public void reset() {
        played = 0;
        correct = 0;
        incorrect = 0;
        bestTime = 0;
        averageTime = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.played);
        dest.writeInt(this.correct);
        dest.writeInt(this.incorrect);
        dest.writeInt(this.perfectInRow);
        dest.writeInt(this.bestTime);
        dest.writeInt(this.averageTime);
    }
}
