package com.rubengees.vocables.pojo;

import android.app.Activity;
import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Todo: Describe Class
 *
 * @author Ruben Gees
 */
@RunWith(AndroidJUnit4.class)
public class UnitTest extends ActivityInstrumentationTestCase2<Activity> {

    private Unit unit;

    public UnitTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() throws Exception {
        unit = new Unit(1, "Title", 15325233);
        List<Vocable> vocables = new ArrayList<>();
        MeaningList meaningList1 = new MeaningList("Test1");
        MeaningList meaningList2 = new MeaningList("Test2");

        vocables.add(new Vocable(meaningList1, meaningList2, "Hint", 823578259));
        vocables.add(new Vocable(meaningList2, meaningList1, "Hint2", 823259));

        unit.addAll(vocables);
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();

        unit.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Unit createdFromParcel = Unit.CREATOR.createFromParcel(parcel);
        assertEquals(unit, createdFromParcel);
    }
}