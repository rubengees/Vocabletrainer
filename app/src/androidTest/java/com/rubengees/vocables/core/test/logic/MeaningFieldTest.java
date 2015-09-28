package com.rubengees.vocables.core.test.logic;

import android.app.Activity;
import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.rubengees.vocables.pojo.MeaningList;
import com.rubengees.vocables.pojo.Vocable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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
        meaningField = new MeaningField(2, 2);

        ArrayList<MeaningCell> cells = new ArrayList<>(2);
        MeaningList meaningList1 = new MeaningList("abc");
        MeaningList meaningList2 = new MeaningList("cba");
        MeaningCell meaningCell1 = new MeaningCell(new Vocable(meaningList1, meaningList2, null,
                123), meaningList1);
        MeaningCell meaningCell2 = new MeaningCell(new Vocable(meaningList2, meaningList1, null,
                321), meaningList2);

        cells.add(meaningCell1);
        cells.add(meaningCell2);
        cells.add(meaningCell2);
        cells.add(meaningCell1);

        meaningField.setCells(cells);
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();

        meaningField.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        MeaningField createdFromParcel = MeaningField.CREATOR.createFromParcel(parcel);
        assertTrue(meaningField.equals(createdFromParcel));
    }
}