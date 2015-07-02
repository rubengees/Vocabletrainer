package com.rubengees.vocables.core.test.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.rubengees.vocables.pojo.MeaningList;
import com.rubengees.vocables.pojo.Vocable;

/**
 * Created by Ruben on 04.04.2015.
 */
public class MeaningCell implements Parcelable {

    public static final Creator<MeaningCell> CREATOR = new Creator<MeaningCell>() {

        public MeaningCell createFromParcel(Parcel in) {
            return new MeaningCell(in);
        }

        public MeaningCell[] newArray(int size) {
            return new MeaningCell[size];
        }
    };

    private Vocable vocable;
    private MeaningList meaningList;

    private MeaningCell(Parcel in) {
        readFromParcel(in);
    }

    public MeaningCell(Vocable vocable, MeaningList meaningList) {
        this.vocable = vocable;
        this.meaningList = meaningList;
    }

    private void readFromParcel(Parcel in) {
        vocable = in.readParcelable(Vocable.class.getClassLoader());
        meaningList = in.readParcelable(MeaningList.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(vocable, flags);
        out.writeParcelable(meaningList, flags);
    }

    public Vocable getVocable() {
        return vocable;
    }

    public MeaningList getMeaningList() {
        return meaningList;
    }
}
