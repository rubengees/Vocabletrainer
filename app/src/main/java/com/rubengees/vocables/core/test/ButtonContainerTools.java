package com.rubengees.vocables.core.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.test.logic.MeaningCell;
import com.rubengees.vocables.core.test.logic.MeaningField;
import com.rubengees.vocables.core.test.logic.Position;
import com.rubengees.vocables.utils.AnimationUtils;
import com.rubengees.vocables.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for the Modes with a Grid of Buttons.x
 */
public class ButtonContainerTools {

    public static final int ANIMATION_DURATION = 300;

    /**
     * Appends a LinearLayout containing buttons as specified in the sizeX and sizeY parameters.
     * To every Button is a Tag with the {@link Position} assigned. A OnClickListener can be set also.
     *
     * @param root           The parent View, which the new one should be added to
     * @param sizeX          The amount of Buttons in horizontal direction
     * @param sizeY          The amount of Buttons in vertical direction
     * @param buttonListener A listener which should be assigned to each Button
     */
    public static void buildButtonLayout(@NonNull ViewGroup root, int sizeX, int sizeY, @Nullable View.OnClickListener buttonListener) {
        List<View> viewsToAnimate = new ArrayList<>();
        Context context = root.getContext();

        for (int i = 0; i < sizeX; i++) {
            LinearLayout container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_test_button_container, root, false);
            root.addView(container);

            for (int ii = 0; ii < sizeY; ii++) {
                Button button = (Button) LayoutInflater.from(context).inflate(R.layout.layout_test_button, container, false);
                container.addView(button);

                button.setOnClickListener(buttonListener);
                viewsToAnimate.add(button);
            }
        }

        AnimationUtils.fadeInParallel(viewsToAnimate, ANIMATION_DURATION, null);
    }

    /**
     * Refreshes a ButtonLayout using the data from a {@link MeaningField}.
     *
     * @param root      The ButtonLayout
     * @param field     The MeaningField
     * @param color     The Color for the Buttons
     * @param darkColor A darker version of the Color
     * @param animateIn If Buttons should animate in
     */
    public static void refreshButtons(@NonNull ViewGroup root, @NonNull MeaningField field, int color, int darkColor, boolean animateIn) {
        List<View> viewsToAnimate = new ArrayList<>();

        for (int i = 0; i < field.getSizeX(); i++) {
            ViewGroup buttonContainer = (ViewGroup) root.getChildAt(i);

            for (int ii = 0; ii < field.getSizeY(); ii++) {
                Position pos = new Position(i, ii);
                MeaningCell cell = field.getCell(pos);

                AppCompatButton button = (AppCompatButton) buttonContainer.getChildAt(ii);

                if (cell == null) {
                    button.setVisibility(View.INVISIBLE);
                    button.setEnabled(false);
                } else {
                    button.setText(cell.getMeaningList().toString());
                    if (field.isSelected(pos)) {
                        Utils.tintButton(button, darkColor);
                    } else {
                        Utils.tintButton(button, color);
                    }

                    button.setVisibility(View.VISIBLE);
                    button.setEnabled(true);

                    if (animateIn) {
                        viewsToAnimate.add(button);
                    }
                }
            }
        }

        if (animateIn) {
            AnimationUtils.fadeInParallel(viewsToAnimate, ANIMATION_DURATION, null);
        }
    }

    /**
     * Returns a Button from the ButtonLayout at the specified {@link Position}.
     *
     * @param layout The ButtonLayout
     * @param pos    The Position
     * @return The Button at that Position. May be null
     */
    @Nullable
    public static Button getButtonAt(@NonNull ViewGroup layout, @NonNull Position pos) {
        return (Button) ((ViewGroup) layout.getChildAt(pos.getX())).getChildAt(pos.getY());
    }

    @Nullable
    public static Position findPositionOfButton(@NonNull ViewGroup layout, @NonNull Button button) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            ViewGroup buttonContainer = (ViewGroup) layout.getChildAt(i);

            for (int ii = 0; ii < buttonContainer.getChildCount(); ii++) {
                if (button == buttonContainer.getChildAt(ii)) {
                    return new Position(i, ii);
                }
            }
        }

        return null;
    }
}
