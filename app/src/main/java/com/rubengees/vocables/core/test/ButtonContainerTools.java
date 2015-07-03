package com.rubengees.vocables.core.test;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.test.logic.MeaningCell;
import com.rubengees.vocables.core.test.logic.MeaningField;
import com.rubengees.vocables.core.test.logic.Position;
import com.rubengees.vocables.utils.AnimationUtils;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by Ruben on 05.04.2015.
 */
public class ButtonContainerTools {

    public static void buildButtonLayout(Context context, ViewGroup root, int sizeX, int sizeY, View.OnClickListener buttonListener) {

        for (int i = 0; i < sizeX; i++) {
            LinearLayout container = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_test_button_container, root, false);
            root.addView(container);

            for (int ii = 0; ii < sizeY; ii++) {
                Button button = (Button) LayoutInflater.from(context).inflate(R.layout.layout_test_button, container, false);
                container.addView(button);

                button.setTag(new Position(i, ii));
                button.setOnClickListener(buttonListener);
                animateIn(button);
            }
        }
    }

    public static void refreshButtons(ViewGroup root, MeaningField field, int color, int darkColor, boolean animateIn) {
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
                    button.setText(cell.getMeaning().toString());
                    if (field.isSelected(pos)) {
                        Utils.setButtonColor(button, darkColor);
                    } else {
                        Utils.setButtonColor(button, color);
                    }

                    button.setTag(new Position(i, ii));
                    button.setVisibility(View.VISIBLE);
                    button.setEnabled(true);

                    if (animateIn) {
                        AnimationUtils.animate(button, Techniques.FadeIn, 300, 0, null);
                    }
                }
            }
        }
    }

    public static Button getButtonAt(ViewGroup layout, Position pos) {
        return (Button) ((ViewGroup) layout.getChildAt(pos.getX())).getChildAt(pos.getY());
    }

    private static void animateIn(View view) {
        AnimationUtils.animate(view, Techniques.FadeIn, 300, 0, null);
    }

}
