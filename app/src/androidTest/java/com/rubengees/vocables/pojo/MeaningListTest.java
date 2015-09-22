package com.rubengees.vocables.pojo;

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
public class MeaningListTest extends ActivityInstrumentationTestCase2<Activity> {

    private MeaningList meaningList;

    public MeaningListTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() throws Exception {
        meaningList = new MeaningList("Test");
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();

        meaningList.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        MeaningList createdFromParcel = MeaningList.CREATOR.createFromParcel(parcel);
        assertEquals(meaningList, createdFromParcel);
    }
}