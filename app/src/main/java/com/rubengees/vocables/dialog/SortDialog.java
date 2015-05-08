package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.enumeration.SortMode;

/**
 * Created by Ruben on 07.05.2015.
 */
public class SortDialog extends DialogFragment {

    private SortMode mode;
    private SortDialogCallback callback;

    public static SortDialog newInstance(SortMode mode) {
        SortDialog dialog = new SortDialog();
        Bundle bundle = new Bundle();

        bundle.putSerializable("sort_mode", mode);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = (SortMode) getArguments().getSerializable("sort_mode");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        final int selectedItem;
        switch (mode) {
            case TITLE:
                selectedItem = 0;
                break;
            case TIME:
                selectedItem = 1;
                break;
            default:
                selectedItem = 0;
                break;
        }

        builder.title("Sort").items(new CharSequence[]{"Title", "Last modification time"}).itemsCallbackSingleChoice(selectedItem, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int index, CharSequence charSequence) {
                SortMode result;

                switch (index) {
                    case 0:
                        result = SortMode.TITLE;
                        break;
                    case 1:
                        result = SortMode.TIME;
                        break;
                    default:
                        result = SortMode.TITLE;
                        break;
                }

                if (result != mode && callback != null) {
                    callback.onSort(result);
                }

                return true;
            }
        });

        return builder.build();
    }

    public void setCallback(SortDialogCallback callback) {
        this.callback = callback;
    }

    public interface SortDialogCallback {
        void onSort(SortMode mode);
    }


}
