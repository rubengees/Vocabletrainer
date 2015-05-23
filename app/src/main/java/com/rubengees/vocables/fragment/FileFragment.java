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
import com.rubengees.vocables.adapter.FileAdapter;
import com.rubengees.vocables.utils.FileComparator;
import com.rubengees.vocables.utils.Filesystem;
import com.rubengees.vocables.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileFragment extends MainFragment implements FileAdapter.OnItemClickListener, View.OnClickListener, MainActivity.OnBackPressedListener {

    private Filesystem filesystem;
    private FileAdapter adapter;

    private TextView status;
    private ImageButton up;

    private FileFragmentListener listener;

    public FileFragment() {

    }

    public static FileFragment newInstance(String path) {
        FileFragment fragment = new FileFragment();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FileFragment withListener(FileFragmentListener listener) {
        this.listener = listener;

        return this;
    }

    public void setFileEventListener(FileFragmentListener listener) {
        this.listener = listener;
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
        View header = inflater.inflate(R.layout.fragment_transfer_header, container, false);
        up = (ImageButton) header.findViewById(R.id.fragment_transfer_header_back);
        status = (TextView) header.findViewById(R.id.fragment_transfer_header_title);
        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.fragment_transfer_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Utils.getSpanCount(getActivity()), StaggeredGridLayoutManager.VERTICAL);
        adapter = new FileAdapter(this);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        up.setOnClickListener(this);
        getToolbarActivity().setToolbarView(header);

        refresh();

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("filesystem", filesystem);
    }

    public File getCurrentDirectory() {
        return filesystem.getCurrentDir();
    }

    private void cd(String name) {
        filesystem.cd(name);
        refresh();
    }

    private void cdUp() {
        filesystem.cdUp();
        refresh();
    }

    private void refresh() {
        refreshToolbar();
        refreshList();
    }

    private void refreshToolbar() {
        if (filesystem.isRoot()) {
            up.setVisibility(View.GONE);
        } else {
            up.setVisibility(View.VISIBLE);
        }

        status.setText(filesystem.getPath());
    }

    private void refreshList() {
        List<File> files = filesystem.getFiles();

        Collections.sort(files, new FileComparator());
        adapter.setFiles(files);
    }

    @Override
    public void onItemClick(File file) {
        if (file.isDirectory()) {
            cd(file.getName());
        } else {
            if (listener != null) {
                listener.onFileClicked(file);
            }
        }
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

    @Override
    public void onClick(View v) {
        if (v == up) {
            cdUp();
        }
    }

    public interface FileFragmentListener {
        void onFileClicked(File file);
    }

}
