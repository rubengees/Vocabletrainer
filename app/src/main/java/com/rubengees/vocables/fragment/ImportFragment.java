package com.rubengees.vocables.fragment;

import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

/**
 * Created by ruben on 19.05.15.
 */
public class ImportFragment extends TransferFragment {


    public static TransferFragment newInstance(String path) {
        ImportFragment fragment = new ImportFragment();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onFileSelected(File file) {
        if (file.getName().endsWith(".xml") || file.getName().endsWith(".csv")) {
            listener.onImport(file);
        } else {
            Toast.makeText(getActivity(), "This filetype is not supported. Read the Help for furher information", Toast.LENGTH_LONG).show();
        }
    }
}
