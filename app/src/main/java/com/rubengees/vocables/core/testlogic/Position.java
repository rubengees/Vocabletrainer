package com.rubengees.vocables.core.testlogic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ruben on 05.04.2015.
 */
public class Position implements Parcelable {

    public static final Creator<Position> CREATOR = new Creator<Position>() {

        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        public Position[] newArray(int size) {
            return new Position[size];
        }

    };
    private int x;
    private int y;

    private Position(Parcel in) {
        readFromParcel(in);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void readFromParcel(Parcel in) {
        x = in.readInt();
        y = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(x);
        out.writeInt(y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof Position) {
            Position pos = (Position) other;

            return pos.getX() == x && pos.getY() == y;
        } else {
            return false;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
