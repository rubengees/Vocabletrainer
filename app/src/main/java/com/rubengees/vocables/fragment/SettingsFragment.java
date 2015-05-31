package com.rubengees.vocables.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.TwoStatePreference;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.view.Display;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.activity.MainActivity;
import com.rubengees.vocables.utils.ReminderUtils;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by Ruben on 24.04.2015.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, MainActivity.OnBackPressedListener {

    private Preference ads, reminder, email, evaluation, developer, licences, source;
    private ListPreference reminderTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getMainActivity().getSupportActionBar();

        if (ab != null) {
            ab.setSubtitle(null);
        }

        getToolbarActivity().collapseToolbar();

        addPreferencesFromResource(R.xml.preferences);
        ads = this.findPreference("pref_ads");
        reminder = this.findPreference("pref_reminder");
        reminderTime = (ListPreference) this.findPreference("pref_reminder_time");
        email = this.findPreference("pref_email");
        evaluation = this.findPreference("pref_evaluation");
        developer = this.findPreference("pref_developer");
        licences = this.findPreference("pref_licences");
        source = this.findPreference("pref_source");

        ads.setOnPreferenceChangeListener(this);
        reminder.setOnPreferenceChangeListener(this);
        reminderTime.setOnPreferenceChangeListener(this);
        email.setOnPreferenceClickListener(this);
        evaluation.setOnPreferenceClickListener(this);
        developer.setOnPreferenceClickListener(this);
        licences.setOnPreferenceClickListener(this);
        source.setOnPreferenceClickListener(this);

        if (!((TwoStatePreference) reminder).isChecked()) {
            reminderTime.setEnabled(false);
        }

        setReminderTimeSummary(reminderTime.getValue());
    }

    private void setReminderTimeSummary(String newValue) {
        String summary = null;
        String[] values = getActivity().getResources().getStringArray(R.array.reminder_time_preference_values);
        String[] entries = getActivity().getResources().getStringArray(R.array.reminder_time_preference_entries);

        for (int i = 0; i < values.length; i++) {
            if (newValue.equals(values[i])) {
                summary = entries[i];
            }
        }

        reminderTime.setSummary(summary);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == ads) {
            if ((boolean) newValue) {
                getMainActivity().showAds();
            } else {
                getMainActivity().hideAds();
            }
        } else if (preference == reminder) {
            if ((boolean) newValue) {
                ReminderUtils.setReminder(getActivity());
                reminderTime.setEnabled(true);
            } else {
                ReminderUtils.cancelReminder(getActivity());
                reminderTime.setEnabled(false);
            }
        } else if (preference == reminderTime) {
            ReminderUtils.cancelReminder(getActivity());
            ReminderUtils.setReminder(getActivity());
            setReminderTimeSummary((String) newValue);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == email) {
            ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity());

            builder.setType("message/rfc822");
            builder.addEmailTo("geesruben@yahoo.de");
            builder.setSubject("Vocabletrainer Feedback");
            builder.setChooserTitle("Choose Client");
            builder.startChooser();

            return true;
        } else if (preference == evaluation) {
            final String appPackageName = getActivity().getPackageName();

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

            return true;
        } else if (preference == developer) {
            Utils.showPlayStorePage(getActivity());
            return true;
        } else if (preference == licences) {
            new LibsBuilder().withAboutIconShown(true).withAboutVersionShownName(true).withAnimations(true)
                    .withAboutAppName("Vocabletrainer")
                    .withAboutDescription("A free and opensource App to learn Vocables").withActivityTitle("Libraries")
                    .withFields(R.string.class.getFields()).withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR).start(getActivity());
        } else if (preference == source) {
            Uri webpage = Uri.parse("https://github.com/RubenGees/Vocabletrainer");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        return false;
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected ExtendedToolbarActivity getToolbarActivity() {
        return (ExtendedToolbarActivity) getActivity();
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float displayWidth = size.x;
        Animator animator = null;

        if(enter) {
            animator = ObjectAnimator.ofFloat(this, "translationX", displayWidth / 4, 0);
        }

        if(animator != null) {
            animator.setDuration(500);
        }

        return animator;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
