package com.rubengees.vocables.core.test.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.rubengees.vocables.pojo.MeaningList;
import com.rubengees.vocables.pojo.Vocable;

/**
 * Created by Ruben on 04.04.2015.
 */
public class MeaningCell implements Parcelable {
    public static final Parcelable.Creator<MeaningCell> CREATOR = new Parcelable.Creator<MeaningCell>() {
        public MeaningCell createFromParcel(Parcel source) {
            return new MeaningCell(source);
        }

        public MeaningCell[] newArray(int size) {
            return new MeaningCell[size];
        }
    };
    private Vocable vocable;
    private MeaningList meaningList;

    public MeaningCell(Vocable vocable, MeaningList meaningList) {
        this.vocable = vocable;
        this.meaningList = meaningList;
    }

    protected MeaningCell(Parcel in) {
        this.vocable = in.readParcelable(Vocable.class.getClassLoader());
        this.meaningList = in.readParcelable(MeaningList.class.getClassLoader());
    }

    public Vocable getVocable() {
        return vocable;
    }

    public MeaningList getMeaningList() {
        return meaningList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeaningCell that = (MeaningCell) o;

        if (vocable != null ? !vocable.equals(that.vocable) : that.vocable != null) return false;
        return !(meaningList != null ? !meaningList.equalsMeanings(that.meaningList) : that.meaningList != null);

    }

    @Override
    public int hashCode() {
        int result = vocable != null ? vocable.hashCode() : 0;
        result = 31 * result + (meaningList != null ? meaningList.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.vocable, 0);
        dest.writeParcelable(this.meaningList, 0);
    }
}
