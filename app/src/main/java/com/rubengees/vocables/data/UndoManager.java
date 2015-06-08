package com.rubengees.vocables.data;

import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by ruben on 08.06.15.
 */
public class UndoManager {

    private HashMap<Integer, Unit> units;
    private int size = 0;

    public UndoManager() {
        this.units = new HashMap<>();
    }

    public void add(Unit unit) {
        Unit dummy = new Unit();
        dummy.setTitle(unit.getTitle());

        dummy.addAll(unit.getVocables());
        units.put(unit.getId(), dummy);

        size += unit.size();
    }

    public void add(Unit unit, Vocable vocable) {
        if (units.containsKey(unit.getId())) {
            units.get(unit.getId()).add(vocable);
        } else {
            Unit dummy = new Unit();
            dummy.setTitle(unit.getTitle());

            dummy.add(vocable);
            units.put(unit.getId(), dummy);
        }

        size++;
    }

    public void clear() {
        units.clear();
        size = 0;
    }

    public Collection<Unit> getUnits() {
        return units.values();
    }

    public int size() {
        return size;
    }

}
