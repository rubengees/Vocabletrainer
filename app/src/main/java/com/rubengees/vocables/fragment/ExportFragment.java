package com.rubengees.vocables.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.rubengees.vocables.activity.TransferActivity;
import com.rubengees.vocables.utils.DrawableType;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by ruben on 19.05.15.
 */
public class ExportFragment extends TransferFragment implements TransferActivity.OnSaveClickedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        getTransferActivity().enableFab(this, Utils.generateDrawable(getActivity(), FontAwesome.Icon.faw_save, DrawableType.GENERIC, android.R.color.white));

        return root;
    }

    @Override
    public void onSaveClicked() {
        listener.onExport(getCurrentDir());
    }
}
