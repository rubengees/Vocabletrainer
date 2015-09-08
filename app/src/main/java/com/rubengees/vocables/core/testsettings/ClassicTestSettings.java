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

    public ClassicTestSettings() {

    }

    public ClassicTestSettings(ArrayList<Integer> unitIds, int maxRate, Direction direction, boolean caseSensitive) {
        super(unitIds, maxRate);
        this.direction = direction;
        this.caseSensitive = caseSensitive;
    }

    protected ClassicTestSettings(Parcel in) {
        super(in);
        int tmpDirection = in.readInt();
        this.direction = tmpDirection == -1 ? null : Direction.values()[tmpDirection];
        this.caseSensitive = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.direction == null ? -1 : this.direction.ordinal());
        dest.writeByte(caseSensitive ? (byte) 1 : (byte) 0);
    }
}
