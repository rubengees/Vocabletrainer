package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class TestSettings implements Parcelable {

    public static final Creator<TestSettings> CREATOR = new Creator<TestSettings>() {
        @Override
        public TestSettings createFromParcel(Parcel in) {
            return new TestSettings(in);
        }

        @Override
        public TestSettings[] newArray(int size) {
            return new TestSettings[size];
        }
    };
    private ArrayList<Integer> unitIds;
    private int maxRate;

    public TestSettings() {
        unitIds = new ArrayList<>();
    }

    public TestSettings(ArrayList<Integer> unitIds, int maxRate) {
        this.unitIds = unitIds;
        this.maxRate = maxRate;
    }

    protected TestSettings(Parcel in) {
        this.unitIds = new ArrayList<>();
        in.readList(this.unitIds, List.class.getClassLoader());
        this.maxRate = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.unitIds);
        dest.writeInt(this.maxRate);
    }

}
