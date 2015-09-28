package com.rubengees.vocables.core.test.logic;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubengees.vocables.pojo.MeaningList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ruben on 04.04.2015.
 */
public class MeaningField implements Iterable<MeaningCell>, Parcelable {

    public static final Parcelable.Creator<MeaningField> CREATOR = new Parcelable.Creator<MeaningField>() {
        public MeaningField createFromParcel(Parcel source) {
            return new MeaningField(source);
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

    public MeaningField(@IntRange(from = 2) int sizeX, @IntRange(from = 2) int sizeY, List<MeaningCell> cells) {
        this(sizeX, sizeY);
        setCells(cells);
    }

    public MeaningField(@IntRange(from = 2) int sizeX, @IntRange(from = 2) int sizeY) {
        field = new MeaningCell[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    protected MeaningField(Parcel in) {
        this.selected = in.readParcelable(Position.class.getClassLoader());
        this.elementCount = in.readInt();
        this.sizeX = in.readInt();
        this.sizeY = in.readInt();
        field = new MeaningCell[sizeX][sizeY];

        List<MeaningCell> meaningCellList = new ArrayList<>(sizeX * sizeY);

        in.readTypedList(meaningCellList, MeaningCell.CREATOR);
        setCells(meaningCellList);
    }

    public void setCells(@NonNull List<MeaningCell> cells) {
        elementCount = 0;

        for (int i = 0; i < sizeX; i++) {
            for (int ii = 0; ii < sizeY; ii++) {
                MeaningCell current = cells.get(i * sizeY + ii);
                field[i][ii] = current;

                if (current != null) {
                    elementCount++;
                }
            }
        }

        unSelect();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSize() {
        return sizeX * sizeY;
    }

    public void unSelect() {
        selected = null;
    }

    public boolean isEmpty() {
        return elementCount <= 0;
    }

    public void remove(@NonNull Position pos) {
        field[pos.getX()][pos.getY()] = null;
        elementCount--;
    }

    public void select(@Nullable Position pos) {
        selected = pos;
    }

    @Nullable
    public Position getSelectedPosition() {
        return selected;
    }

    @Nullable
    public MeaningCell getSelected() {
        if (selected == null) {
            return null;
        } else {
            return field[selected.getX()][selected.getY()];
        }
    }

    @Nullable
    public Position findCellPosition(@NonNull MeaningList meaningList) {
        for (int i = 0; i < sizeX; i++) {
            for (int ii = 0; ii < sizeY; ii++) {
                Position current = new Position(i, ii);
                MeaningCell cell = getCell(current);

                if (cell != null && cell.getMeaningList().equalsMeanings(meaningList)) {
                    return current;
                }
            }
        }

        return null;
    }

    public MeaningCell getCell(@NonNull Position pos) {
        return field[pos.getX()][pos.getY()];
    }

    public boolean isSelected(@NonNull Position pos) {
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

        for (int i = 0; i < sizeX; i++) {
            for (int ii = 0; ii < sizeY; ii++) {
                MeaningCell current = field[i][ii];

                if (current != null) {
                    result.add(current);
                }
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeaningField that = (MeaningField) o;

        if (elementCount != that.elementCount) return false;
        if (sizeX != that.sizeX) return false;
        if (sizeY != that.sizeY) return false;
        if (!Arrays.deepEquals(field, that.field)) return false;
        return !(selected != null ? !selected.equals(that.selected) : that.selected != null);

    }

    @Override
    public int hashCode() {
        int result = field != null ? Arrays.deepHashCode(field) : 0;
        result = 31 * result + (selected != null ? selected.hashCode() : 0);
        result = 31 * result + elementCount;
        result = 31 * result + sizeX;
        result = 31 * result + sizeY;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.selected, 0);
        dest.writeInt(this.elementCount);
        dest.writeInt(this.sizeX);
        dest.writeInt(this.sizeY);

        List<MeaningCell> meaningCellList = new ArrayList<>(sizeX * sizeY);

        for (int i = 0; i < sizeX; i++) {
            meaningCellList.addAll(Arrays.asList(field[i]));
        }

        dest.writeTypedList(meaningCellList);
    }
}
