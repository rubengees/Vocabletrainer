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

    public static int calculateCorrectAnswerRate(int correct, int incorrect) {
        if (correct == 0 && incorrect == 0) {
            return 100; //100%
        } else {
            return (int) ((double) correct / ((double) correct + (double) incorrect) * 100);
        }
    }

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
     * @param context The Context.
     * @return True if the device is a tablet, false otherwise.
     */
    private static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

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
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onWaitFinished();
                        }
                    });
                }
            }
        }).start();
    }

    public static void showPlayStorePage(Context context) {
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

    public static int dpToPx(Context context, int dp) {
        Resources resources = context.getResources();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static void setButtonColor(AppCompatButton button, int color) {
        ColorStateList list = new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{android.R.attr.state_pressed}}, new int[]{color, darkenColor(color)});
        button.setSupportBackgroundTintList(list);
    }

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    public static int getColor(Context context, @ColorRes int color) {
        return context.getResources().getColor(color);
    }

    public interface OnWaitFinishedListener {
        void onWaitFinished();
    }

}
