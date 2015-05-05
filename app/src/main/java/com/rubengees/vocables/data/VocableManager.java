package com.rubengees.vocables.data;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruben on 24.04.2015.
 */
public class VocableManager {

    private HashMap<Integer, Unit> units;
    private Database db;

    public VocableManager(Context context) {
        db = new Database(context);
        try {
            units = db.getUnits();
        } catch (SQLiteException e) {
            db.clear();
        }
    }

    public void vocableAdded(Unit unit, Vocable vocable) {
        db.addVocable(unit, vocable);
    }

    public void vocablesAdded(Unit unit, List<Vocable> vocables) {
        db.addVocables(unit, vocables);
    }

    public void vocableRemoved(Unit unit, Vocable vocable) {
        if (unit.isEmpty()) {
            units.remove(unit.getId());
            db.removeUnit(unit);
        }

        db.removeVocable(vocable);
    }

    public void vocablesRemoved(Unit unit, List<Vocable> vocables) {
        if (unit.isEmpty()) {
            units.remove(unit.getId());
            db.removeUnit(unit);
        }

        db.removeVocables(vocables);
    }

    public void updateVocable(Unit oldUnit, Unit newUnit, Vocable vocable) {
        if (oldUnit == newUnit) {
            db.updateVocable(oldUnit, vocable);
        } else {
            oldUnit.remove(vocable);
            if (oldUnit.isEmpty()) {
                units.remove(oldUnit.getId());
                db.removeUnit(oldUnit);
            }

            newUnit.add(vocable);
            vocableAdded(newUnit, vocable);
        }
    }

    public void updateVocablesFast(List<Vocable> vocables) {
        db.updateVocablesFast(vocables);
    }

    public void addUnit(Unit unit) {
        if (unit.isEmpty()) {
            throw new RuntimeException("A unit cannot be empty!");
        }

        if (unit.getId() == null) {
            Unit sameTitle = null;

            for (Unit unit1 : units.values()) {
                if (unit1.getTitle().equals(unit.getTitle())) {
                    sameTitle = unit1;
                }
            }

            if (sameTitle == null) {
                db.addUnit(unit);
                units.put(unit.getId(), unit);
                vocablesAdded(unit, unit.getVocables());
            } else {
                vocablesAdded(sameTitle, unit.getVocables());
            }
        } else {
            vocablesAdded(units.get(unit.getId()), unit.getVocables());
        }
    }

    public void addUnits(List<Unit> units) {
        for (Unit unit : units) {
            addUnit(unit);
        }
    }

    public void removeUnit(Unit unit) {
        List<Vocable> vocables = unit.getVocables();

        unit.clear();
        vocablesRemoved(unit, vocables);
    }

    public Unit getUnit(int id) {
        return units.get(id);
    }

    public List<Unit> getUnitList() {
        return new ArrayList<>(units.values());
    }

    public Map<Integer, Unit> getUnitMap() {
        return new HashMap<>(units);
    }

    public void clear() {
        units.clear();
        db.clear();
    }

    public int getCount() {
        return units.size();
    }
}
