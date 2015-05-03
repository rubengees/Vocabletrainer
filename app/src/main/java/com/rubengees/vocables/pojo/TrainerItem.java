package com.rubengees.vocables.pojo;

/**
 * Created by Ruben on 24.04.2015.
 */
public interface TrainerItem {

    Long getLastModificationTime();

    void setLastModificationTime(long time);

    Integer getId();

    void setId(int id);

    int getCorrect();

    int getIncorrect();

}
