package com.rubengees.vocables.core.test.logic;

import android.content.Context;
import android.os.Bundle;

import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ruben on 04.04.2015.
 */
public class PairTestLogic extends TestLogic<TestSettings> {

    private static final String STATE_FIELD = "field";

    private MeaningField field;

    public PairTestLogic(Context context, TestSettings settings, int sizeX, int sizeY) {
        super(context, settings);
        this.field = new MeaningField(sizeX, sizeY);

        next();
    }

    public PairTestLogic(Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
    }

    @Override
    public boolean next() {
        super.next();

        if (field.isEmpty()) {
            if (getPosition() + (field.getSize() / 2) <= getAmount()) {
                List<MeaningCell> meaningCells = new ArrayList<>(10);

                for (Vocable vocable : getCurrentVocables()) {
                    meaningCells.add(new MeaningCell(vocable, vocable.getFirstMeaning()));
                    meaningCells.add(new MeaningCell(vocable, vocable.getSecondMeaning()));
                }

                Collections.shuffle(meaningCells);

                field.setCells(meaningCells);

                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);
        field = savedInstanceState.getParcelable(STATE_FIELD);
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        outState.putParcelable(STATE_FIELD, field);
    }

    @Override
    public String getHint() {
        MeaningCell selected = field.getSelected();

        if (selected == null) {
            return null;
        } else {
            Vocable current = selected.getVocable();

            if (current != null) {
                return selected.getVocable().getHint();
            } else {
                return null;
            }
        }
    }

    private List<Vocable> getCurrentVocables() {
        return getSubList(getPosition(), getPosition() + field.getSize() / 2);
    }

    public Position processAnswer(Position pos) {
        MeaningCell selectedCell = field.getSelected();
        MeaningCell givenCell = field.getCell(pos);
        Meaning question = selectedCell.getMeaning();
        Meaning answer = selectedCell.getVocable().getOtherMeaning(question);
        Meaning given = givenCell.getMeaning();
        Vocable vocable = selectedCell.getVocable();

        boolean correct = answer.equals(given);

        processAnswer(vocable, question, answer, given, correct);

        field.remove(field.getSelectedPosition());
        field.unSelect();

        if (correct) {
            field.remove(pos);
            return null;
        } else {
            Position correctPos = field.findCellPosition(answer);

            field.remove(correctPos);
            return correctPos;
        }
    }

    public MeaningField getField() {
        return field;
    }

    public void select(Position pos) {
        field.select(pos);
    }

    public void unSelect() {
        field.unSelect();
    }

    public boolean hasSelectedButton() {
        return field.getSelected() != null;
    }

    public Position getSelected() {
        return field.getSelectedPosition();
    }
}
