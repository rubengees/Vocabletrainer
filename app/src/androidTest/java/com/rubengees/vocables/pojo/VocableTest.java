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
public class VocableTest extends ActivityInstrumentationTestCase2<Activity> {

    private Vocable vocable;

    public VocableTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() throws Exception {
        MeaningList meaningList1 = new MeaningList("Test1");
        MeaningList meaningList2 = new MeaningList("Test2");

        vocable = new Vocable(meaningList1, meaningList2, "Hint", 783752895);
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();

        vocable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Vocable createdFromParcel = Vocable.CREATOR.createFromParcel(parcel);
        assertEquals(vocable, createdFromParcel);
    }
}