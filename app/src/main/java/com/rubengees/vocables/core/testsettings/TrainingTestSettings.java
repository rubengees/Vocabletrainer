package com.rubengees.vocables.core.testsettings;

import android.os.Parcel;

/**
 * Created by Ruben on 20.07.2015.
 */
public class TrainingTestSettings extends TimeTestSettings {

    public static final Creator<TrainingTestSettings> CREATOR = new Creator<TrainingTestSettings>() {
        public TrainingTestSettings createFromParcel(Parcel source) {
            return new TrainingTestSettings(source);
        }

        public TrainingTestSettings[] newArray(int size) {
            return new TrainingTestSettings[size];
        }
    };

    public TrainingTestSettings() {
    }

    protected TrainingTestSettings(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
