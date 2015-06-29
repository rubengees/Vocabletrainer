package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ruben on 04.05.2015.
 */
public class TimeTestSettings extends TestSettings {

    public static final Parcelable.Creator<TimeTestSettings> CREATOR = new Parcelable.Creator<TimeTestSettings>() {

        public TimeTestSettings createFromParcel(Parcel in) {
            return new TimeTestSettings(in);
        }

        public TimeTestSettings[] newArray(int size) {
            return new TimeTestSettings[size];
        }

    };
    private Direction direction;

    public TimeTestSettings() {

    }

    public TimeTestSettings(List<Integer> unitIds, int maxRate, Direction direction) {
        super(unitIds, maxRate);
        this.direction = direction;
    }

    public TimeTestSettings(final Parcel in) {
        super(in);
        direction = (Direction) in.readSerializable();
    }

    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        super.writeToParcel(out, flags);
        out.writeSerializable(direction);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
