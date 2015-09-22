package com.rubengees.vocables.core.test.logic;

import android.app.Activity;
import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Todo: Describe Class
 *
 * @author Ruben Gees
 */
@RunWith(AndroidJUnit4.class)
public class MeaningFieldTest extends ActivityInstrumentationTestCase2<Activity> {

    private MeaningField meaningField;

    public MeaningFieldTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() throws Exception {
        meaningField = new MeaningField(3, 3);
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();

        meaningField.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        MeaningField createdFromParcel = MeaningField.CREATOR.createFromParcel(parcel);
        assertEquals(meaningField, createdFromParcel);
    }
}