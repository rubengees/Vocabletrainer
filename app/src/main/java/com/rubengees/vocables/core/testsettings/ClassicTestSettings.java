package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;

import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class ClassicTestSettings extends TestSettings {

    private Direction direction;
    private boolean caseSensitive;

    public ClassicTestSettings() {

    }

    public ClassicTestSettings(List<Integer> unitIds, int maxRate, Direction direction, boolean caseSensitive) {
        super(unitIds, maxRate);
        this.direction = direction;
        this.caseSensitive = caseSensitive;
    }

    public ClassicTestSettings(final Parcel in) {
        super(in);
        direction = (Direction) in.readSerializable();
        caseSensitive = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        super.writeToParcel(out, flags);
        out.writeSerializable(direction);
        out.writeInt(caseSensitive ? 1 : 0);
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
}