package com.rubengees.vocables.data;

import android.content.Context;
import android.support.annotation.NonNull;

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

    public void vocableAdded(@NonNull Unit unit, @NonNull Vocable vocable) {
        db.addVocable(unit, vocable);
    }

    public void vocablesAdded(@NonNull Unit unit, @NonNull List<Vocable> vocables) {
        db.addVocables(unit, vocables);
    }

    public void vocableRemoved(@NonNull Unit unit, @NonNull Vocable vocable) {
        if (unit.isEmpty()) {
            units.remove(unit.getId());
            db.removeUnit(unit);
        }

        db.removeVocable(vocable);
    }

    public void vocablesRemoved(@NonNull Unit unit, @NonNull List<Vocable> vocables) {
        if (unit.isEmpty()) {
            units.remove(unit.getId());
            db.removeUnit(unit);
        }

        db.removeVocables(vocables);
    }

    public void updateVocable(@NonNull Unit oldUnit, @NonNull Unit newUnit, @NonNull Vocable vocable) {
        if (oldUnit != newUnit) {
            oldUnit.remove(vocable);
            newUnit.add(vocable);
            Unit found = getUnitFromRAM(newUnit);

            if (found != newUnit) {
                found.addAll(newUnit.getVocables());
                newUnit.clear();
            }

            if (oldUnit.isEmpty()) {
                units.remove(oldUnit.getId());
                db.removeUnit(oldUnit);
            }

            db.updateVocable(found, vocable);
        } else {
            db.updateVocable(newUnit, vocable);
        }
    }

    private Unit getUnitFromRAM(@NonNull Unit toFind) {
        Unit existing = units.get(toFind.getId());

        if (existing == null) {
            Unit sameTitle = null;

            for (Unit unit1 : units.values()) {
                if (unit1.getTitle().equals(toFind.getTitle())) {
                    sameTitle = unit1;
                    break;
                }
            }

            if (sameTitle == null) {
                db.addUnit(toFind);
                units.put(toFind.getId(), toFind);

                return toFind;
            } else {
                return sameTitle;
            }
        } else {
            return existing;
        }
    }

    public void updateVocablesFast(@NonNull List<Vocable> vocables) {
        db.updateVocablesFast(vocables);
    }

    public boolean addUnit(@NonNull Unit unit) {
        if (unit.isEmpty()) {
            throw new RuntimeException("A unit cannot be empty!");
        }

        Unit found = getUnitFromRAM(unit);

        if (found != unit) {
            found.addAll(unit.getVocables());
            vocablesAdded(found, unit.getVocables());
            unit.clear();

            return true;
        } else {
            vocablesAdded(found, unit.getVocables());
            return false;
        }
    }

    public void addUnits(@NonNull Collection<Unit> units) {
        for (Unit unit : units) {
            addUnit(unit);
        }
    }

    public void removeUnit(@NonNull Unit unit) {
        List<Vocable> vocables = unit.getVocables();

        unit.clear();
        vocablesRemoved(unit, vocables);
    }

    public Unit getUnit(@NonNull Integer id) {
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

    public void updateUnit(Unit unit) {
        db.updateUnit(unit);
    }
}
