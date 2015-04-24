package com.rubengees.vocables.utils;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Utils {

    public static int calculateCorrectAnswerRate(int correct, int incorrect){
        if (correct == 0 && incorrect == 0) {
            return 100; //100%
        } else {
            return (int) ((double) correct / ((double) correct + (double) incorrect) * 100);
        }
    }

}
