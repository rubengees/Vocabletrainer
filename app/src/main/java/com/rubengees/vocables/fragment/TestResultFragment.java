package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.adapter.ResultAdapter;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.GoogleServiceConnection;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.core.mode.TimeMode;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.Vocable;
import com.rubengees.vocables.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestResultFragment extends MainFragment {

    private static final String KEY_MODE = "mode";
    private static final String KEY_RESULT = "result";
    private static final String KEY_SETTINGS = "settings";
    private static final String KEY_VOCABLES = "vocables";
    private static final int ACHIEVEMENT_TIME = 2000;
    private static final int ACHIEVEMENT_AMOUNT = 20;
    private static final int RATE_PERFECT = 100;
    private static final int RATE_VERY_GOOD = 92;
    private static final int RATE_GOOD = 81;
    private static final int RATE_OKAY = 67;
    private static final int RATE_NOT_SO_WELL = 50;
    private static final int RATE_BAD = 30;

    private Mode mode;
    private TestResult result;
    private TestSettings settings;

    public static TestResultFragment newInstance(Mode mode, TestResult result, TestSettings settings, ArrayList<Vocable> vocables) {
        TestResultFragment fragment = new TestResultFragment();
        Bundle args = new Bundle();

        args.putParcelable(KEY_MODE, mode);
        args.putParcelable(KEY_RESULT, result);
        args.putParcelable(KEY_SETTINGS, settings);
        args.putParcelableArrayList(KEY_VOCABLES, vocables);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Core core = Core.getInstance(getActivity());
        List<Vocable> vocables = new ArrayList<>();

        if (getArguments() != null) {
            mode = getArguments().getParcelable(KEY_MODE);
            result = getArguments().getParcelable(KEY_RESULT);
            settings = getArguments().getParcelable(KEY_SETTINGS);
            vocables = getArguments().getParcelableArrayList(KEY_VOCABLES);
        }

        if (savedInstanceState == null) {
            mode.processResult(result);
            core.getVocableManager().updateVocablesFast(vocables);
            core.saveMode(mode);

            GoogleServiceConnection connection = Core.getInstance(getActivity()).getConnection();

            if (result.getCorrect() >= vocables.size()) {
                if (result.getIncorrect() <= 0) {
                    connection.unlockAchievement(getString(R.string.achievement_perfect));
                }

                connection.unlockAchievement(getString(R.string.achievement_learning));
                connection.incrementAchievement(getString(R.string.achievement_geek));

                if (mode instanceof TimeMode && result.getAverageTime() <= ACHIEVEMENT_TIME
                        && Utils.calculateCorrectAnswerRate(result.getCorrect(), result.getIncorrect()) >= ACHIEVEMENT_AMOUNT) {
                    connection.unlockAchievement(getString(R.string.achievement_at_the_speed_of_light));
                }
            }

            if (mode.getPerfectInRow() == 3) {
                connection.unlockAchievement(getString(R.string.achievement_perfectionist));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recycler = (RecyclerView) inflater.inflate(R.layout.fragment_test_result, container, false);
        View header = inflater.inflate(R.layout.header, container, false);
        TextView resultText = (TextView) header.findViewById(R.id.header_text);
        ResultAdapter adapter = new ResultAdapter(result, getActivity());

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);

        getToolbarActivity().expandToolbar();
        getToolbarActivity().setToolbarView(header);
        getToolbarActivity().enableFab(R.drawable.ic_again, new ExtendedToolbarActivity.OnFabClickListener() {
            @Override
            public void onFabClick() {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.content, TestFragment.newInstance(mode, settings)).commit();
            }
        });

        resultText.setText(calculateResultText());

        return recycler;
    }

    private String calculateResultText() {
        if (result.getCorrect() + result.getIncorrect() > 0) {
            int rate = Utils.calculateCorrectAnswerRate(result.getCorrect(), result.getIncorrect());

            if (rate == RATE_PERFECT) {
                return getString(R.string.fragment_result_perfect);
            } else if (rate >= RATE_VERY_GOOD) {
                return getString(R.string.fragment_result_very_good);
            } else if (rate >= RATE_GOOD) {
                return getString(R.string.fragment_result_good);
            } else if (rate >= RATE_OKAY) {
                return getString(R.string.fragment_result_okay);
            } else if (rate >= RATE_NOT_SO_WELL) {
                return getString(R.string.fragment_result_not_so_well);
            } else if (rate >= RATE_BAD) {
                return getString(R.string.fragment_result_bad);
            } else {
                return getString(R.string.fragment_result_horrible);
            }
        } else {
            return getString(R.string.fragment_result_no_answers);
        }
    }

}
