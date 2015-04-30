package com.rubengees.vocables.pojo;

/**
 * Created by Ruben on 24.04.2015.
 */
public interface TrainerItem {

    public Long getLastModificationTime();

    public void setLastModificationTime(long time);

    public Long getId();

    public void setId(long id);

    public int getCorrect();

    public int getIncorrect();

}
