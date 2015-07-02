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
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
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
 * A Fragment to displays the Settings of this App.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, MainActivity.OnBackPressedListener {

    private static final String PREF_ADS = "pref_ads";
    private static final String PREF_REMINDER = "pref_reminder";
    private static final String PREF_REMINDER_TIME = "pref_reminder_time";
    private static final String PREF_EMAIL = "pref_email";
    private static final String PREF_EVALUATION = "pref_evaluation";
    private static final String PREF_LICENCES = "pref_licences";
    private static final String PREF_DEVELOPER = "pref_developer";
    private static final String PREF_SOURCE = "pref_source";
    private static final int ANIMATION_DURATION = 500;

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
        ads = this.findPreference(PREF_ADS);
        reminder = this.findPreference(PREF_REMINDER);
        reminderTime = (ListPreference) this.findPreference(PREF_REMINDER_TIME);
        email = this.findPreference(PREF_EMAIL);
        evaluation = this.findPreference(PREF_EVALUATION);
        developer = this.findPreference(PREF_DEVELOPER);
        licences = this.findPreference(PREF_LICENCES);
        source = this.findPreference(PREF_SOURCE);

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
            builder.setSubject(getString(R.string.fragment_settings_mail_subject));
            builder.setChooserTitle(getString(R.string.fragment_settings_mail_client));
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
            Utils.showGooglePlusPage(getActivity());
            return true;
        } else if (preference == licences) {
            new LibsBuilder().withAboutIconShown(true).withAboutVersionShownName(true).withAnimations(true)
                    .withAboutAppName(getString(R.string.app_name))
                    .withAboutDescription(getString(R.string.activity_about_content)).withActivityTitle(getString(R.string.activity_about_title))
                    .withFields(R.string.class.getFields()).withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR).start(getActivity());
        } else if (preference == source) {
            Uri uri = Uri.parse("https://github.com/RubenGees/Vocabletrainer");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        return false;
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    //Code below is copied from MainFragment since this Fragment can't inherit from it

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

        if (enter) {
            animator = ObjectAnimator.ofFloat(this, "translationX", displayWidth / 4, 0);
            animator.setDuration(ANIMATION_DURATION);
            animator.setInterpolator(new LinearOutSlowInInterpolator());
        }

        return animator;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
