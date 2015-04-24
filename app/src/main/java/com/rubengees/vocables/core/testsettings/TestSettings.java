package com.rubengees.vocables.core.testsettings;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class TestSettings {

    private List<Integer> unitIds;
    private int maxRate;

    public TestSettings() {
        unitIds = new LinkedList<>();
    }

    public TestSettings(List<Integer> unitIds, int maxRate) {
        this.unitIds = unitIds;
        this.maxRate = maxRate;
    }

    public void addUnitId(int id){
        unitIds.add(id);
    }

    public void setMaxRate(int rate){
        this.maxRate = rate;
    }

    public List<Integer> getUnitIds() {
        return unitIds;
    }

    public int getMaxRate() {
        return maxRate;
    }
}
