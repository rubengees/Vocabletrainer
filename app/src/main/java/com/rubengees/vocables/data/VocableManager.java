package com.rubengees.vocables.data;

import android.content.Context;

import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.Collection;
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
        } catch (Exception e) {
            db.clear();
            units = new HashMap<>();
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
        if (oldUnit != newUnit) {
            oldUnit.remove(vocable);
            newUnit.add(vocable);
            if (!units.containsKey(newUnit.getId())) {
                units.put(newUnit.getId(), newUnit);
                db.addUnit(newUnit);
            }
            if (oldUnit.isEmpty()) {
                units.remove(oldUnit.getId());
                db.removeUnit(oldUnit);
            }
        }

        db.updateVocable(newUnit, vocable);
    }

    public void updateVocablesFast(List<Vocable> vocables) {
        db.updateVocablesFast(vocables);
    }

    public boolean addUnit(Unit unit) {
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
                sameTitle.addAll(unit.getVocables());
                vocablesAdded(sameTitle, unit.getVocables());

                return true;
            }
        } else {
            Unit existing = units.get(unit.getId());

            existing.addAll(unit.getVocables());
            vocablesAdded(existing, unit.getVocables());

            return true;
        }

        return false;
    }

    public void addUnits(Collection<Unit> units) {
        for (Unit unit : units) {
            addUnit(unit);
        }
    }

    public void removeUnit(Unit unit) {
        List<Vocable> vocables = unit.getVocables();

        unit.clear();
        vocablesRemoved(unit, vocables);
    }

    public Unit getUnit(Integer id) {
        if (id == null) {
            return null;
        } else {
            return units.get(id);
        }
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

    public void updateUnit(Unit unit) {
        db.updateUnit(unit);
    }
}
