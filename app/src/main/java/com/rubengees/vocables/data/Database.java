package com.rubengees.vocables.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ruben Gees on 06.02.2015.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE = "vocables.db";
    private static final int VERSION = 5;
    private static final String TABLE_VOCABLES = "vocables";
    private static final String TABLE_UNITS = "units";
    private static final String TABLE_UNIT_VOCABLE = "unit_vocable";
    private static final String TABLE_MEANINGS1 = "meanings1";
    private static final String TABLE_MEANINGS2 = "meanings2";
    private static final String COLUMN_VOCABLE_ID = "vocable_id";
    private static final String COLUMN_VOCABLE_CORRECT = "vocable_correct";
    private static final String COLUMN_VOCABLE_INCORRECT = "vocable_answered";
    private static final String COLUMN_VOCABLE_HINT = "vocable_hint";
    private static final String COLUMN_VOCABLE_CREATION_TIME = "vocable_creation_time";
    private static final String CREATE_TABLE_VOCABLES = "create table " + TABLE_VOCABLES + " ( " + COLUMN_VOCABLE_ID + " integer primary key autoincrement, " + COLUMN_VOCABLE_CORRECT + " integer, " + COLUMN_VOCABLE_INCORRECT + " integer, " + COLUMN_VOCABLE_HINT + " text, " + COLUMN_VOCABLE_CREATION_TIME + " long);";
    private static final String COLUMN_UNIT_ID = "unit_id";
    private static final String COLUMN_UNIT_TITLE = "unit_title";
    private static final String COLUMN_UNIT_CREATION_TIME = "unit_creation_time";
    private static final String CREATE_TABLE_UNITS = "create table " + TABLE_UNITS + " ( " + COLUMN_UNIT_ID + " integer primary key autoincrement, " + COLUMN_UNIT_TITLE + " text, " + COLUMN_UNIT_CREATION_TIME + " long);";
    private static final String COLUMN_UNIT_VOCABLE_U_ID = "unit_id";
    private static final String COLUMN_UNIT_VOCABLE_V_ID = "vocable_id";
    private static final String CREATE_TABLE_UNIT_VOCABLE = "create table " + TABLE_UNIT_VOCABLE + " ( " + COLUMN_UNIT_VOCABLE_U_ID + " integer, " + COLUMN_UNIT_VOCABLE_V_ID + " integer);";
    private static final String COLUMN_MEANING1_ID = "meaning1_id";
    private static final String COLUMN_MEANING1_MEANING = "meaning1_data";
    private static final String CREATE_TABLE_MEANINGS1 = "create table " + TABLE_MEANINGS1 + " ( " + COLUMN_MEANING1_ID + " integer, " + COLUMN_MEANING1_MEANING + " text);";
    private static final String COLUMN_MEANING2_ID = "meaning2_id";
    private static final String COLUMN_MEANING2_MEANING = "meaning2_data";
    private static final String CREATE_TABLE_MEANINGS2 = "create table " + TABLE_MEANINGS2 + " ( " + COLUMN_MEANING2_ID + " integer, " + COLUMN_MEANING2_MEANING + " text);";

    public Database(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VOCABLES);
        db.execSQL(CREATE_TABLE_UNITS);
        db.execSQL(CREATE_TABLE_UNIT_VOCABLE);
        db.execSQL(CREATE_TABLE_MEANINGS1);
        db.execSQL(CREATE_TABLE_MEANINGS2);
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion <= 4) {
            HashMap<String, ArrayList<Vocable>> vocables = new HashMap<>();
            long time = System.currentTimeMillis();

            Cursor cursor = db.query("vocables", null, null, null, null, null, "id ASC");
            Cursor meanings1Cursor = null;
            Cursor meanings2Cursor = null;
            if (oldVersion > 2) {
                meanings1Cursor = db.query("values1", null, null, null, null, null, "id_values1 ASC");
                meanings2Cursor = db.query("values2", null, null, null, null, null, "id_values2 ASC");
                meanings1Cursor.moveToFirst();
                meanings2Cursor.moveToFirst();
            }
            if (cursor.moveToFirst()) {
                do {
                    String unitTitle;
                    ArrayList<String> meanings1;
                    ArrayList<String> meanings2;
                    int id = cursor.getInt(0);
                    int correct;
                    int incorrect;
                    String hint = null;

                    if (oldVersion <= 2) {
                        meanings1 = new ArrayList<>();
                        meanings2 = new ArrayList<>();

                        meanings1.add(cursor.getString(1));
                        meanings2.add(cursor.getString(2));
                    } else {
                        meanings1 = generateMeanings(id, meanings1Cursor);
                        meanings2 = generateMeanings(id, meanings2Cursor);
                    }

                    if (oldVersion <= 2) {
                        correct = cursor.getInt(4);
                        incorrect = cursor.getInt(3) - correct;
                    }else{
                        correct = cursor.getInt(2);
                        incorrect = cursor.getInt(1) - correct;
                    }

                    if (oldVersion == 4) {
                        hint = cursor.getString(4);
                    }

                    if (oldVersion <= 1) {
                        unitTitle = "Default";
                    } else {
                        unitTitle = cursor.getString(3);
                    }

                    Vocable vocable = new Vocable(id, new Meaning(meanings1), new Meaning(meanings2), correct, incorrect, hint, time);

                    if (vocables.containsKey(unitTitle)) {
                        vocables.get(unitTitle).add(vocable);
                    } else {
                        ArrayList<Vocable> list = new ArrayList<>();
                        vocables.put(unitTitle, list);
                        list.add(vocable);
                    }

                } while (cursor.moveToNext());
            }

            cursor.close();
            if (meanings1Cursor != null) {
                meanings1Cursor.close();
            }
            if (meanings2Cursor != null) {
                meanings2Cursor.close();
            }

            db.execSQL("DROP TABLE IF EXISTS vocables");
            db.execSQL("DROP TABLE IF EXISTS units");
            db.execSQL("DROP TABLE IF EXISTS values1");
            db.execSQL("DROP TABLE IF EXISTS values2");

            db.execSQL(CREATE_TABLE_VOCABLES);
            db.execSQL(CREATE_TABLE_UNITS);
            db.execSQL(CREATE_TABLE_UNIT_VOCABLE);
            db.execSQL(CREATE_TABLE_MEANINGS1);
            db.execSQL(CREATE_TABLE_MEANINGS2);

            List<Unit> units = new ArrayList<>();

            for (String key : vocables.keySet()) {
                Unit unit = new Unit();

                unit.setTitle(key);
                unit.addAll(vocables.get(key));
                units.add(unit);
            }

            addUnits(db, units);
            for (Unit unit : units) {
                addVocables(db, unit, unit.getVocables());
            }
        }
    }

    private ArrayList<String> generateMeanings(int id, Cursor cursor) {
        ArrayList<String> result = new ArrayList<>();

        do {
            if (cursor.getInt(0) != id) {
                break;
            }
            result.add(cursor.getString(1));
        } while (cursor.moveToNext());

        return result;
    }

    private void addUnits(SQLiteDatabase db, List<Unit> units) {
        db.beginTransaction();
        for (Unit unit : units) {
            addUnit(db, unit);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void addVocables(SQLiteDatabase db, Unit containingUnit, List<Vocable> vocables) {
        db.beginTransaction();
        for (Vocable vocable : vocables) {
            addVocable(db, containingUnit, vocable);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void addUnit(SQLiteDatabase db, Unit unit) {
        ContentValues unitValues = new ContentValues(2);
        unitValues.put(COLUMN_UNIT_TITLE, unit.getTitle());
        unitValues.put(COLUMN_UNIT_CREATION_TIME, unit.getLastModificationTime());

        int newId = (int) db.insert(TABLE_UNITS, null, unitValues);
        unit.setId(newId);
    }

    private void addVocable(SQLiteDatabase db, Unit containingUnit, Vocable vocable) {
        ContentValues vocableValues = new ContentValues(4);
        ContentValues unitVocableValues = new ContentValues(2);

        vocableValues.put(COLUMN_VOCABLE_CORRECT, vocable.getCorrect());
        vocableValues.put(COLUMN_VOCABLE_INCORRECT, vocable.getIncorrect());
        vocableValues.put(COLUMN_VOCABLE_HINT, vocable.getHint());
        vocableValues.put(COLUMN_VOCABLE_CREATION_TIME, vocable.getLastModificationTime());

        int id = (int) db.insert(TABLE_VOCABLES, null, vocableValues);
        vocable.setId(id);

        insertMeanings(db, id, vocable.getFirstMeaning(), COLUMN_MEANING1_ID, COLUMN_MEANING1_MEANING, TABLE_MEANINGS1);
        insertMeanings(db, id, vocable.getSecondMeaning(), COLUMN_MEANING2_ID, COLUMN_MEANING2_MEANING, TABLE_MEANINGS2);

        unitVocableValues.put(COLUMN_UNIT_VOCABLE_U_ID, containingUnit.getId());
        unitVocableValues.put(COLUMN_UNIT_VOCABLE_V_ID, id);
        db.insert(TABLE_UNIT_VOCABLE, null, unitVocableValues);
    }

    private void insertMeanings(SQLiteDatabase db, int id, Meaning meaning, String columnId, String columnMeaning, String table) {
        for (String m : meaning.getMeanings()) {
            ContentValues meaningValues = new ContentValues(2);

            meaningValues.put(columnId, id);
            meaningValues.put(columnMeaning, m);
            db.insert(table, null, meaningValues);
        }
    }

    public final void addVocable(Unit containingUnit, Vocable vocable) {
        SQLiteDatabase db = this.getWritableDatabase();
        addVocable(db, containingUnit, vocable);
        db.close();
    }

    public final void addVocables(Unit containingUnit, List<Vocable> vocables) {
        SQLiteDatabase db = this.getWritableDatabase();
        addVocables(db, containingUnit, vocables);
        db.close();
    }

    public final void addUnit(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        addUnit(db, unit);
        db.close();
    }

    public final void addUnits(List<Unit> units) {
        SQLiteDatabase db = this.getWritableDatabase();
        addUnits(db, units);
        db.close();
    }

    public final void removeVocable(Vocable vocable) {
        SQLiteDatabase db = this.getWritableDatabase();

        removeVocables(db, new String[]{String.valueOf(vocable.getId())});
        db.close();
    }

    private void removeVocables(SQLiteDatabase db, String[] ids) {
        db.delete(TABLE_VOCABLES, generateWhereClause(COLUMN_VOCABLE_ID, ids), null);
        db.delete(TABLE_UNIT_VOCABLE, generateWhereClause(COLUMN_UNIT_VOCABLE_V_ID, ids), null);
        db.delete(TABLE_MEANINGS1, generateWhereClause(COLUMN_MEANING1_ID, ids), null);
        db.delete(TABLE_MEANINGS2, generateWhereClause(COLUMN_MEANING2_ID, ids), null);
    }

    private String generateWhereClause(String column, String[] ids) {
        String result = column + " in (";

        if (ids.length > 0) {
            result += ids[0];
            for (int i = 1; i < ids.length; i++) {
                result += "," + ids[i];
            }
        }
        result += ")";

        return result;
    }

    public final void removeVocables(List<Vocable> vocables) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] ids = new String[vocables.size()];

        for (int i = 0; i < vocables.size(); i++) {
            ids[i] = String.valueOf(vocables.get(i).getId());
        }
        removeVocables(db, ids);
        db.close();
    }

    public final void removeUnit(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] id = new String[]{String.valueOf(unit.getId())};

        db.delete(TABLE_UNITS, COLUMN_UNIT_ID + " = ?", id);
        db.close();
    }

    public final void updateVocable(Unit containingUnit, Vocable vocable) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] id = new String[]{String.valueOf(vocable.getId())};
        ContentValues vocableValues = new ContentValues(4);
        ContentValues unitVocableValues = new ContentValues(1);

        vocableValues.put(COLUMN_VOCABLE_CORRECT, vocable.getCorrect());
        vocableValues.put(COLUMN_VOCABLE_INCORRECT, vocable.getIncorrect());
        vocableValues.put(COLUMN_VOCABLE_HINT, vocable.getHint());
        vocableValues.put(COLUMN_VOCABLE_CREATION_TIME, System.currentTimeMillis());

        db.update(TABLE_VOCABLES, vocableValues, COLUMN_VOCABLE_ID + " = ?", id);

        db.delete(TABLE_MEANINGS1, COLUMN_MEANING1_ID + " = ?", id);
        db.delete(TABLE_MEANINGS2, COLUMN_MEANING2_ID + " = ?", id);

        insertMeanings(db, vocable.getId(), vocable.getFirstMeaning(), COLUMN_MEANING1_ID, COLUMN_MEANING1_MEANING, TABLE_MEANINGS1);
        insertMeanings(db, vocable.getId(), vocable.getSecondMeaning(), COLUMN_MEANING2_ID, COLUMN_MEANING2_MEANING, TABLE_MEANINGS2);

        unitVocableValues.put(COLUMN_UNIT_VOCABLE_U_ID, containingUnit.getId());

        db.update(TABLE_UNIT_VOCABLE, unitVocableValues, COLUMN_UNIT_VOCABLE_V_ID + " = ?", id);
        db.close();
    }

    public final void updateVocableFast(Vocable vocable) {
        SQLiteDatabase db = this.getWritableDatabase();
        updateVocableFast(db, vocable);
        db.close();
    }

    public void updateVocablesFast(List<Vocable> vocables) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (Vocable vocable : vocables) {
            updateVocableFast(vocable);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void updateVocableFast(SQLiteDatabase db, Vocable vocable) {
        String[] id = new String[]{String.valueOf(vocable.getId())};
        ContentValues vocableValues = new ContentValues(2);

        vocableValues.put(COLUMN_VOCABLE_CORRECT, vocable.getCorrect());
        vocableValues.put(COLUMN_VOCABLE_INCORRECT, vocable.getIncorrect());
        db.update(TABLE_VOCABLES, vocableValues, COLUMN_VOCABLE_ID + " = ?", id);
    }

    public final void updateUnit(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] id = new String[]{String.valueOf(unit.getId())};
        ContentValues unitValues = new ContentValues(2);

        unitValues.put(COLUMN_UNIT_TITLE, unit.getTitle());
        unitValues.put(COLUMN_UNIT_CREATION_TIME, System.currentTimeMillis());

        db.update(TABLE_UNITS, unitValues, COLUMN_UNIT_ID + " = ?", id);
        db.close();
    }

    public final void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOCABLES, null, null);
        db.delete(TABLE_UNITS, null, null);
        db.delete(TABLE_UNIT_VOCABLE, null, null);
        db.delete(TABLE_MEANINGS1, null, null);
        db.delete(TABLE_MEANINGS2, null, null);
        db.close();
    }

    public final HashMap<Integer, Unit> getUnits() {
        HashMap<Integer, Unit> result = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor units = db.query(TABLE_UNITS, null, null, null, null, null, null);
        Cursor vocables = db.query(TABLE_VOCABLES, null, null, null, null, null, COLUMN_VOCABLE_ID + " ASC");
        Cursor unitVocable = db.query(TABLE_UNIT_VOCABLE, null, null, null, null, null, COLUMN_UNIT_VOCABLE_V_ID + " ASC");
        Cursor meanings1 = db.query(TABLE_MEANINGS1, null, null, null, null, null, COLUMN_MEANING1_ID + " ASC");
        Cursor meanings2 = db.query(TABLE_MEANINGS2, null, null, null, null, null, COLUMN_MEANING2_ID + " ASC");

        if (units.moveToFirst() && vocables.moveToFirst() && unitVocable.moveToFirst() && meanings1.moveToFirst() && meanings2.moveToFirst()) {
            result = getUnitMap(units);

            do {
                int id = vocables.getInt(0);
                int correct = vocables.getInt(1);
                int incorrect = vocables.getInt(2);
                String hint = vocables.getString(3);
                long lastModificationTime = vocables.getLong(4);
                Meaning first = new Meaning(generateMeanings(id, meanings1));
                Meaning second = new Meaning(generateMeanings(id, meanings2));

                Vocable vocable = new Vocable(id, first, second, correct, incorrect, hint, lastModificationTime);

                int unitId = getUnitIdForVocable(unitVocable);

                result.get(unitId).add(vocable);
            } while (vocables.moveToNext());

            units.close();
            vocables.close();
            unitVocable.close();
            meanings1.close();
            meanings2.close();
        }

        return result;
    }

    private HashMap<Integer, Unit> getUnitMap(Cursor cursor) {
        HashMap<Integer, Unit> result = new HashMap<>();

        do {
            Unit unit = new Unit();

            unit.setId(cursor.getInt(0));
            unit.setTitle(cursor.getString(1));
            unit.setLastModificationTime(cursor.getInt(2));
            result.put(unit.getId(), unit);
        } while (cursor.moveToNext());

        return result;
    }

    private int getUnitIdForVocable(Cursor unitVocable) {
        int result = unitVocable.getInt(0);

        unitVocable.moveToNext();
        return result;
    }

}
