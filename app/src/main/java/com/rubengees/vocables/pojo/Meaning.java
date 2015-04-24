package com.rubengees.vocables.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Meaning implements Serializable {

    private List<String> meanings;

    public Meaning(List<String> meanings) {
        if (meanings.isEmpty()) {
            throw new RuntimeException("The list with the meanings can not be empty");
        }
        this.meanings = meanings;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> firstMeanings) {
        this.meanings = firstMeanings;
    }

    public boolean contains(Object o) {
        return meanings.contains(o);
    }

    public boolean containsIgnoreCase(Object o) {
        if (o instanceof String) {
            for (String meaning : meanings) {
                if (meaning.equalsIgnoreCase((String) o)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object another) {
        if (another == this) {
            return true;
        }

        if (another instanceof Meaning) {
            List<String> copy = new ArrayList<>(meanings);

            for (String s : ((Meaning) another).getMeanings()) {
                if (!copy.remove(s)) {
                    return false;
                }
            }

            return copy.size() == 0;
        } else {
            return false;
        }

    }

    public boolean equalsIgnoreCase(Object another) {
        if (another == this) {
            return true;
        }

        if (another instanceof Meaning) {
            List<String> copy = new ArrayList<>(meanings);

            for (String s : ((Meaning) another).getMeanings()) {
                for (int i = 0; i < copy.size(); i++) {
                    if (copy.get(i).equalsIgnoreCase(s)) {
                        copy.remove(i);
                    }
                }
            }

            return copy.size() == 0;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result = "";

        if (meanings.size() > 0) {
            result += meanings.get(0);
        }

        for (int i = 1; i < meanings.size(); i++) {
            result += "/";
            result += meanings.get(i);
        }

        return result;
    }
}
