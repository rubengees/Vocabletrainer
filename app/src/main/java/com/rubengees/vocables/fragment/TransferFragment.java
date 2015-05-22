package com.rubengees.vocables.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.MainActivity;
import com.rubengees.vocables.activity.TransferActivity;
import com.rubengees.vocables.adapter.FileAdapter;
import com.rubengees.vocables.utils.FileComparator;
import com.rubengees.vocables.utils.Filesystem;
import com.rubengees.vocables.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransferFragment extends MainFragment implements FileAdapter.OnItemClickListener, View.OnClickListener, MainActivity.OnBackPressedListener {

    protected OnFinishedListener listener;
    private Filesystem filesystem;
    private FileAdapter adapter;

    private TextView status;
    private ImageButton up;

    public TransferFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            filesystem = savedInstanceState.getParcelable("filesystem");
        } else if (getArguments() != null) {
            filesystem = new Filesystem(getArguments().getString("path"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfer, container, false);
        View header = inflater.inflate(R.layout.fragmnet_transfer_header, container, false);
        up = (ImageButton) header.findViewById(R.id.fragment_transfer_header_back);
        status = (TextView) header.findViewById(R.id.fragment_transfer_header_title);
        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.fragment_transfer_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Utils.getSpanCount(getActivity()), StaggeredGridLayoutManager.VERTICAL);
        adapter = new FileAdapter(this);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        up.setOnClickListener(this);
        getTransferActivity().setToolbarView(header);

        refreshList();
        refreshStatus();

        return root;
    }

    protected File getCurrentDir() {
        return new File(filesystem.getPath());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("filesystem", filesystem);
    }

    public void setListener(OnFinishedListener listener) {
        this.listener = listener;
    }

    private void cd(String name) {
        filesystem.cd(name);
        refreshList();
        refreshStatus();
    }

    private void cdUp() {
        filesystem.cdUp();
        refreshList();
        refreshStatus();
    }

    private void refreshList() {
        List<File> files = filesystem.getFiles();

        Collections.sort(files, new FileComparator());
        adapter.setFiles(files);

        if (filesystem.isRoot()) {
            up.setVisibility(View.GONE);
        } else {
            up.setVisibility(View.VISIBLE);
        }
    }

    private void refreshStatus() {
        status.setText(filesystem.getPath());
    }

    @Override
    public void onItemClick(File file) {
        if (file.isDirectory()) {
            cd(file.getName());
        } else {
            onFileSelected(file);
        }
    }

    public void onFileSelected(File file) {

    }

    @Override
    public boolean onBackPressed() {
        if (filesystem.isRoot()) {
            return true;
        } else {
            cdUp();
            return false;
        }
    }

    protected TransferActivity getTransferActivity() {
        return (TransferActivity) getActivity();
    }

    @Override
    public void onClick(View v) {
        cdUp();
    }

    public interface OnFinishedListener {
        void onImport(File file);

        void onExport(File file);
    }
}
