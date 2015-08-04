package com.rubengees.vocables.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private static final String KEY_PATH = "path";
    private static final String KEY_FILESYSTEM = "filesystem";

    private Filesystem filesystem;
    private FileAdapter adapter;

    private TextView status;
    private ImageButton up;

    private FileFragmentListener listener;

    public FileFragment() {

    }

    public static FileFragment newInstance(@NonNull String path) {
        FileFragment fragment = new FileFragment();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setFileEventListener(@Nullable FileFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            filesystem = savedInstanceState.getParcelable(KEY_FILESYSTEM);
        } else if (getArguments() != null) {
            filesystem = new Filesystem(getArguments().getString(KEY_PATH));
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
        getToolbarActivity().expandToolbar();
        getToolbarActivity().setToolbarView(header);

        refresh();

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_FILESYSTEM, filesystem);
        super.onSaveInstanceState(outState);
    }

    public File getCurrentDirectory() {
        return filesystem.getCurrentDir();
    }

    private void cd(@NonNull String name) {
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
        int verticalMargin = Utils.dpToPx(getActivity(), 8);
        int horizontalMargin = Utils.dpToPx(getActivity(), 16);

        if (filesystem.isRoot()) {
            up.setVisibility(View.GONE);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) status.getLayoutParams();
            params.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
            status.requestLayout();
        } else {
            up.setVisibility(View.VISIBLE);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) status.getLayoutParams();
            params.setMargins(0, verticalMargin, horizontalMargin, verticalMargin);
            status.requestLayout();
        }

        status.setText(filesystem.getPath());
    }

    private void refreshList() {
        List<File> files = filesystem.getFiles();

        Collections.sort(files, new FileComparator());
        adapter.setFiles(files);
    }

    @Override
    public void onItemClick(@NonNull File file) {
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
