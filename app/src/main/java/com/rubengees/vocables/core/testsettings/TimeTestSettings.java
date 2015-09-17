package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Ruben on 04.05.2015.
 */
public class TimeTestSettings extends TestSettings {

    public static final Creator<TimeTestSettings> CREATOR = new Creator<TimeTestSettings>() {
        public TimeTestSettings createFromParcel(Parcel source) {
            return new TimeTestSettings(source);
        }

        public TimeTestSettings[] newArray(int size) {
            return new TimeTestSettings[size];
        }
    };
    private Direction direction;

    public TimeTestSettings() {

    }

    public TimeTestSettings(ArrayList<Integer> unitIds, int maxRate, Direction direction) {
        super(unitIds, maxRate);
        this.direction = direction;
    }

    protected TimeTestSettings(Parcel in) {
        super(in);
        int tmpDirection = in.readInt();
        this.direction = tmpDirection == -1 ? null : Direction.values()[tmpDirection];
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.direction == null ? -1 : this.direction.ordinal());
    }
}
