package com.rubengees.vocables.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatButton;
import android.util.TypedValue;

import com.rubengees.vocables.R;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Utils {

    private static final float DARKER_COLOR_FACTOR = 0.9f;

    /**
     * Calculates the rate of correct answers in relation to the incorrect ones.
     * The Result is returned as an Integer in percent. (0 >= x >= 100)
     * If correct and incorrect are both 0, 100 is returned.
     *
     * @param correct   The amount of correct Answers
     * @param incorrect The amount of incorrect Answers
     * @return The Result
     */
    public static int calculateCorrectAnswerRate(int correct, int incorrect) {
        if (correct == 0 && incorrect == 0) {
            return 100; //100%
        } else {
            return (int) ((double) correct / ((double) correct + (double) incorrect) * 100);
        }
    }

    /**
     * Returns the best amount of spans for UI-Elements with support for grids.
     * I.e. a RecyclerView with a GridLayoutManager.
     *
     * @param context The current Context
     * @return The amount of Spans
     */
    public static int getSpanCount(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Utils.isTablet(context)) {
                return 3;
            } else {
                return 2;
            }
        } else {
            if (Utils.isTablet(context)) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    /**
     * Checks if the device is a Tablet.
     *
     * @param context The Context
     * @return True if the device is a tablet, false otherwise
     */
    private static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    /**
     * Waits for the passed amount of time and calls the Callback afterwards.0
     * It is safe to run UI-Code in the Callback Method.
     *
     * @param context  The current Context
     * @param time     The amount of time in Milliseconds
     * @param listener The Callback
     */
    public static void wait(final Activity context, final int time, final OnWaitFinishedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (context != null) {
                    context.runOnUiThread(new MyRunnable(listener));
                }
            }
        }).start();
    }

    /**
     * Opens the Google+ page from the author of this App.
     *
     * @param context The current Context.
     */
    public static void showGooglePlusPage(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", "+RubenGeesistDerBoss");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + "+RubenGeesistDerBoss" + "/posts")));
        }
    }

    /**
     * Converts dp to actual px.
     *
     * @param context The current Context
     * @param dp      The dp
     * @return Th px
     */
    public static int dpToPx(Context context, int dp) {
        Resources resources = context.getResources();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    /**
     * Tints a Button with the desired Color.
     *
     * @param button The Button to tint
     * @param color  The Color to use
     */
    public static void setButtonColor(AppCompatButton button, int color) {
        ColorStateList list = new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{android.R.attr.state_pressed}}, new int[]{color, darkenColor(color)});
        button.setSupportBackgroundTintList(list);
    }

    /**
     * Darkens a color using the hsv-representation.
     *
     * @param color The Color to darken
     * @return The new Color
     */
    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= DARKER_COLOR_FACTOR;
        return Color.HSVToColor(hsv);
    }

    /**
     * Return a Color from the Resources
     *
     * @param context The current Context
     * @param color   The ID of the Color
     * @return The Color
     */
    public static int getColor(Context context, @ColorRes int color) {
        return context.getResources().getColor(color);
    }

    /**
     * Callback for the {@link Utils#wait(Activity, int, OnWaitFinishedListener)}-Method.
     */
    public interface OnWaitFinishedListener {
        void onWaitFinished();
    }

    private static class MyRunnable implements Runnable {
        private final OnWaitFinishedListener listener;

        public MyRunnable(OnWaitFinishedListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.onWaitFinished();
        }
    }

}
