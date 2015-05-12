package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.testsettings.layout.ClassicTestSettingsLayout;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;

/**
 * Created by ruben on 27.04.15.
 */
public class ClassicMode extends Mode {


    public static final Parcelable.Creator<ClassicMode> CREATOR = new Parcelable.Creator<ClassicMode>() {

        public ClassicMode createFromParcel(Parcel in) {
            return new ClassicMode(in);
        }

        public ClassicMode[] newArray(int size) {
            return new ClassicMode[size];
        }

    };

    public ClassicMode(Parcel in) {
        super(in);
    }

    public ClassicMode(ModeData data) {
        super(data);
    }

    @Override
    public int getColor(final Context context) {
        return context.getResources().getColor(R.color.classic_mode);
    }

    @Override
    public int getDarkColor(final Context context) {
        return context.getResources().getColor(R.color.classic_mode_dark);
    }

    @Override
    public int getMinAmount() {
        return 1;
    }

    @Override
    public String getHelpText(final Context context) {
        return "Test";
    }

    @Override
    public String getTitle(final Context context) {
        return "Classic Mode";
    }

    @Override
    public String getShortTitle(final Context context) {
        return "Classic";
    }

    @Override
    public Drawable getIcon(final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_classic);
    }

    @Override
    public TestSettingsLayout getTestSettingsLayout(Context context, TestSettingsLayout.OnTestSettingsListener listener) {
        return new ClassicTestSettingsLayout(context, listener);
    }

    @Override
    public Test getTest() {
        return null;
    }
}
