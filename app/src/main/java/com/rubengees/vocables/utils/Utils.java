package com.rubengees.vocables.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Window;

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

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onWaitFinished();
                    }
                });
            }
        }).start();
    }

    public static void colorWindow(Activity activity, int color, int darkColor) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setNavigationBarColor(color);
            window.setStatusBarColor(darkColor);
        }
    }

    public interface OnWaitFinishedListener {
        void onWaitFinished();
    }

}
