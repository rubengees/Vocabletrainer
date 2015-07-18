package com.rubengees.vocables.pojo;

/**
 * Created by Ruben on 24.04.2015.
 */
public interface TrainerItem {

    /**
     * Returns the time of last modification.
     *
     * @return The last modification time
     */
    Long getLastModificationTime();

    /**
     * Updates the modification time to the current time.
     */
    void updateModificationTime();

    /**
     * Returns the ID of this Object.
     * @return The ID
     */
    Integer getId();

    /**
     * Sets a new ID to this Object.
     * @param id The new ID
     */
    void setId(int id);

    /**
     * Returns the amount of correct answers of this Object.
     * @return The amount of correct answers
     */
    int getCorrect();

    /**
     * Returns the amount of incorrect answers of this Object.
     * @return The amount of incorrect answers
     */
    int getIncorrect();

}