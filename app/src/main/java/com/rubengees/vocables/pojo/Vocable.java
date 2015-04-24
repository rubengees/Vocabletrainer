package com.rubengees.vocables.pojo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Vocable implements TrainerItem, Serializable {

    private Long id;
    private Meaning firstMeaning;
    private Meaning secondMeaning;
    private String hint;
    private long lastModificationTime;

    private int correct;
    private int incorrect;

    public Vocable(@NonNull Meaning firstMeaning, @NonNull Meaning secondMeaning, @Nullable String hint, long lastModificationTime) {
        this.firstMeaning = firstMeaning;
        this.secondMeaning = secondMeaning;
        this.hint = hint;
        this.lastModificationTime = lastModificationTime;
        this.correct = 0;
        this.incorrect = 0;
    }

    public Vocable(@NonNull Long id, @NonNull Meaning firstMeaning, @NonNull Meaning secondMeaning, int correct, int incorrect, @Nullable String hint, long lastModificationTime) {
        this.id = id;
        this.firstMeaning = firstMeaning;
        this.secondMeaning = secondMeaning;
        this.hint = hint;
        this.lastModificationTime = lastModificationTime;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    @Override
    public long getLastModificationTime() {
        return lastModificationTime;
    }

    @Override
    public void setLastModificationTime(long time) {
        this.lastModificationTime = time;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return firstMeaning.toString();
    }

    @Override
    public int getCorrect() {
        return correct;
    }

    @Override
    public int getIncorrect() {
        return incorrect;
    }

    public Meaning getFirstMeaning() {
        return firstMeaning;
    }

    public Meaning getSecondMeaning() {
        return secondMeaning;
    }

    public void processAnswer(boolean correct) {
        if (correct) {
            this.correct++;
        } else {
            this.incorrect++;
        }
    }

    public String getHint() {
        return hint;
    }

    public void setHint(@Nullable String hint) {
        this.hint = hint;
    }
}
