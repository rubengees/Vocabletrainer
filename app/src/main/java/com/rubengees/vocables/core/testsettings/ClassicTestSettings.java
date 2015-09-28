package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Ruben on 24.04.2015.
 */
public class ClassicTestSettings extends TestSettings {

    public static final Creator<ClassicTestSettings> CREATOR = new Creator<ClassicTestSettings>() {
        public ClassicTestSettings createFromParcel(Parcel source) {
            return new ClassicTestSettings(source);
        }

        public ClassicTestSettings[] newArray(int size) {
            return new ClassicTestSettings[size];
        }
    };
    private Direction direction;
    private boolean caseSensitive;
    private boolean allMeanings;

    public ClassicTestSettings() {

    }

    public ClassicTestSettings(ArrayList<Integer> unitIds, int maxRate, Direction direction,
                               boolean caseSensitive, boolean allMeanings) {
        super(unitIds, maxRate);
        this.direction = direction;
        this.caseSensitive = caseSensitive;
        this.allMeanings = allMeanings;
    }

    protected ClassicTestSettings(Parcel in) {
        super(in);
        int tmpDirection = in.readInt();
        this.direction = tmpDirection == -1 ? null : Direction.values()[tmpDirection];
        this.caseSensitive = in.readByte() != 0;
        this.allMeanings = in.readByte() != 0;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isAllMeanings() {
        return allMeanings;
    }

    public void setAllMeanings(boolean allMeanings) {
        this.allMeanings = allMeanings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.direction == null ? -1 : this.direction.ordinal());
        dest.writeByte(caseSensitive ? (byte) 1 : (byte) 0);
        dest.writeByte(allMeanings ? (byte) 1 : (byte) 0);
    }
}
