package com.rubengees.vocables.core.test.logic;

import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Vocable;

import java.io.Serializable;

/**
 * Created by Ruben on 04.04.2015.
 */
public class MeaningCell implements Serializable {

    private Vocable vocable;
    private Meaning meaning;

    public MeaningCell(Vocable vocable, Meaning meaning) {
        this.vocable = vocable;
        this.meaning = meaning;
    }

    public Vocable getVocable() {
        return vocable;
    }

    public Meaning getMeaning() {
        return meaning;
    }
}
