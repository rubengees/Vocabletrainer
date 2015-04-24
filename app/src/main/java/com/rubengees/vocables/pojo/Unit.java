package com.rubengees.vocables.pojo;

import com.rubengees.vocables.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Unit implements TrainerItem, Serializable {

    private Long id;
    private String title;
    private List<Vocable> vocables;
    private long lastModificationTime;

    public Unit(Long id, String title, long lastModificationTime) {
        this();
        this.id = id;
        this.title = title;
        this.lastModificationTime = lastModificationTime;
    }

    public Unit() {
        vocables = new ArrayList<>();
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
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getCorrect() {
        int result = 0;

        for (Vocable vocable : vocables) {
            result += vocable.getCorrect();
        }

        return result;
    }

    @Override
    public int getIncorrect() {
        int result = 0;

        for (Vocable vocable : vocables) {
            result += vocable.getIncorrect();
        }

        return result;
    }

    public List<Vocable> getVocables() {
        return vocables;
    }

    public List<Vocable> getVocables(int maxRate) {
        ArrayList<Vocable> result = new ArrayList<>();

        for (Vocable vocable : vocables) {
            if (Utils.calculateCorrectAnswerRate(vocable.getCorrect(), vocable.getIncorrect()) > maxRate) {
                continue;
            }

            result.add(vocable);
        }

        return result;
    }

    public int size() {
        return vocables.size();
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public void add(Vocable vocable) {
        vocables.add(vocable);
    }

    public void addAll(Collection<Vocable> vocablesToAdd) {
        vocables.addAll(vocablesToAdd);
    }

    public void clear() {
        vocables.clear();
    }

    public void remove(int pos) {
        vocables.remove(pos);
    }

    public boolean remove(Vocable vocable) {
        return vocables.remove(vocable);
    }
}
