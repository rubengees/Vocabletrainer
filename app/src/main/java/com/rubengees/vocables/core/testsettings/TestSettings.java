package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class TestSettings implements Parcelable {

    public static final Parcelable.Creator<TestSettings> CREATOR = new Parcelable.Creator<TestSettings>() {

        public TestSettings createFromParcel(Parcel in) {
            return new TestSettings(in);
        }

        public TestSettings[] newArray(int size) {
            return new TestSettings[size];
        }

    };

    private List<Integer> unitIds;
    private int maxRate;

    public TestSettings() {
        unitIds = new LinkedList<>();
    }

    public TestSettings(Parcel in) {
        readFromParcel(in);
    }

    public TestSettings(List<Integer> unitIds, int maxRate) {
        this.unitIds = unitIds;
        this.maxRate = maxRate;
    }

    public void addUnitId(int id) {
        unitIds.add(id);
    }

    public List<Integer> getUnitIds() {
        return unitIds;
    }

    public int getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(int rate) {
        this.maxRate = rate;
    }

    private void readFromParcel(Parcel in) {
        unitIds = new ArrayList<>();
        in.readList(unitIds, Integer.class.getClassLoader());
        maxRate = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(unitIds);
        out.writeInt(maxRate);
    }
}
