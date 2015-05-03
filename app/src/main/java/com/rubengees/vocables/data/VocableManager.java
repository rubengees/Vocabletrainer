package com.rubengees.vocables.data;

import android.content.Context;
import android.os.AsyncTask;

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
        units = db.getUnits();
    }

    public void addVocable(final Unit unit, final Vocable vocable) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                checkUnit(unit);
                db.addVocable(unit, vocable);
                return null;
            }
        }.execute();
    }

    public void addVocables(final Unit unit, final List<Vocable> vocables) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                checkUnit(unit);
                db.addVocables(unit, vocables);
                return null;
            }
        }.execute();
    }

    public void addUnit(Unit unit) {
        addVocables(unit, unit.getVocables());
    }

    public void updateVocable(final Unit newUnit, final Unit oldUnit, final Vocable vocable) {
        if (newUnit != oldUnit) {
            checkUnit(newUnit);
            newUnit.add(vocable);
            oldUnit.remove(vocable);
            if (oldUnit.isEmpty()) {
                removeUnit(oldUnit);
            }
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                db.updateVocable(newUnit, vocable);
                return null;
            }
        }.execute();
    }

    public void updateVocableFast(final Vocable vocable) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                db.updateVocableFast(vocable);
                return null;
            }
        }.execute();
    }

    public void updateUnit(final Unit unit) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                db.updateUnit(unit);
                return null;
            }
        }.execute();
    }

    public void removeVocable(final Unit unit, final Vocable vocable) {
        if (unit.isEmpty()) {
            removeUnit(unit);
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                db.removeVocable(vocable);
                return null;
            }
        }.execute();
    }

    public void removeUnit(final Unit unit) {
        final List<Vocable> vocables = unit.getVocables();

        if (!unit.isEmpty()) {
            unit.clear();
        }
        units.remove(unit.getId());

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!vocables.isEmpty()) {
                    db.removeVocables(vocables);
                }
                db.removeUnit(unit);
                return null;
            }
        }.execute();
    }

    public List<Unit> getUnitList() {
        return new ArrayList<>(units.values());
    }

    public Map<Integer, Unit> getUnitMap() {
        return new HashMap<>(units);
    }

    public void clear() {
        units.clear();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.clear();
                return null;
            }
        }.execute();
    }

    public int getCount() {
        return units.size();
    }

    private void checkUnit(Unit unit) {
        if (unit.getId() == null || !units.containsKey(unit.getId())) {
            db.addUnit(unit);
            units.put(unit.getId(), unit);
        }
    }
}
