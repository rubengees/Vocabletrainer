package com.rubengees.vocables.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.TransferActivity;
import com.rubengees.vocables.utils.Filesystem;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransferFragment extends Fragment {

    protected TransferActivity.OnFinishedListener listener;
    private Filesystem filesystem;

    public TransferFragment() {

    }

    public static TransferFragment newInstance(String path) {
        TransferFragment fragment = new TransferFragment();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            filesystem = new Filesystem(getArguments().getString("path"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    public void setListener(TransferActivity.OnFinishedListener listener) {
        this.listener = listener;
    }
}
