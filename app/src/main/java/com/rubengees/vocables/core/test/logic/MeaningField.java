package com.rubengees.vocables.core.test.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.rubengees.vocables.pojo.Meaning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ruben on 04.04.2015.
 */
public class MeaningField implements Iterable<MeaningCell>, Parcelable {

    public static final Creator<MeaningField> CREATOR = new Creator<MeaningField>() {

        public MeaningField createFromParcel(Parcel in) {
            return new MeaningField(in);
        }

        public MeaningField[] newArray(int size) {
            return new MeaningField[size];
        }

    };
    private MeaningCell[][] field;
    private Position selected;
    private int elementCount = 0;

    private int sizeX;
    private int sizeY;

    public MeaningField(int sizeX, int sizeY, List<MeaningCell> cells) {
        this(sizeX, sizeY);
        setCells(cells);
    }

    public MeaningField(int sizeX, int sizeY) {
        field = new MeaningCell[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    private MeaningField(Parcel in) {
        readFromParcel(in);
    }

    public void setCells(List<MeaningCell> cells) {
        for (int i = 0; i < getSizeX(); i++) {
            for (int ii = 0; ii < getSizeY(); ii++) {
                field[i][ii] = cells.get(i * getSizeY() + ii);
            }
        }

        elementCount = getSize();
        unSelect();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSize() {
        return getSizeX() * getSizeY();
    }

    public void unSelect() {
        selected = null;
    }

    private void readFromParcel(Parcel in) {
        sizeX = in.readInt();
        sizeY = in.readInt();
        field = new MeaningCell[sizeX][sizeY];
        for (int i = 0; i < sizeY; i++) {
            field[i] = in.createTypedArray(MeaningCell.CREATOR);
        }
        selected = in.readParcelable(Position.class.getClassLoader());
        elementCount = in.readInt();
    }

    public boolean isEmpty() {
        return elementCount <= 0;
    }

    public void remove(Position pos) {
        field[pos.getX()][pos.getY()] = null;
        elementCount--;
    }

    public void select(Position pos) {
        selected = pos;
    }

    public Position getSelectedPosition() {
        return selected;
    }

    public MeaningCell getSelected() {
        if (selected == null) {
            return null;
        } else {
            return field[selected.getX()][selected.getY()];
        }
    }

    public Position findCellPosition(Meaning meaning) {
        for (int i = 0; i < getSizeX(); i++) {
            for (int ii = 0; ii < getSizeY(); ii++) {
                Position current = new Position(i, ii);
                MeaningCell cell = getCell(current);

                if (cell != null && cell.getMeaning().equals(meaning)) {
                    return current;
                }
            }
        }

        return null;
    }

    public MeaningCell getCell(Position pos) {
        return field[pos.getX()][pos.getY()];
    }

    public boolean isSelected(Position pos) {
        return selected != null && selected.equals(pos);
    }

    public MeaningCell[][] toArray() {
        return field;
    }

    @Override
    public Iterator<MeaningCell> iterator() {
        return toList().iterator();
    }

    public List<MeaningCell> toList() {
        List<MeaningCell> result = new ArrayList<>(getSize());

        for (int i = 0; i < getSizeX(); i++) {
            for (int ii = 0; ii < getSizeY(); ii++) {
                MeaningCell current = field[i][ii];

                if (current != null) {
                    result.add(current);
                }
            }
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getSizeX());
        out.writeInt(getSizeY());

        for (MeaningCell[] cell : field) {
            out.writeParcelableArray(cell, 0);
        }

        out.writeParcelable(selected, 0);
        out.writeInt(elementCount);
    }

}
