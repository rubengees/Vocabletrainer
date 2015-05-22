package com.rubengees.vocables.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.activity.TransferActivity;

/**
 * Created by ruben on 19.05.15.
 */
public class ExportFragment extends TransferFragment implements TransferActivity.OnSaveClickedListener {

    public static TransferFragment newInstance(String path) {
        ExportFragment fragment = new ExportFragment();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveClicked() {
        listener.onExport(getCurrentDir());
    }
}
