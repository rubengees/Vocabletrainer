package com.rubengees.vocables.core;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.rubengees.vocables.R;
import com.rubengees.vocables.dialog.GoogleServiceErrorDialog;
import com.rubengees.vocables.utils.PreferenceUtils;

/**
 * Created by Ruben on 24.04.2015.
 */
public class GoogleServiceConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleServiceErrorDialog.GoogleServiceErrorDialogCallback {

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final int REQUEST_ACHIEVEMENTS = 1002;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final String STATE_SHOULD_CONNECT = "should_connect";
    private static final String GOOGLE_SERVICE_ERROR_DIALOG = "google_service_error_dialog";

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError;
    private Activity context;

    private boolean shouldConnect;

    public GoogleServiceConnection(Activity context, Bundle savedInstanceState) {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .setViewForPopups(context.findViewById(R.id.content))
                .build();

        this.context = context;

        if (savedInstanceState != null) {
            mResolvingError = savedInstanceState.getBoolean(STATE_RESOLVING_ERROR);
            shouldConnect = savedInstanceState.getBoolean(STATE_SHOULD_CONNECT);
        } else {
            shouldConnect = PreferenceUtils.shouldSignIn(context);
        }

        GoogleServiceErrorDialog dialog = (GoogleServiceErrorDialog) context.getFragmentManager().findFragmentByTag(GOOGLE_SERVICE_ERROR_DIALOG);

        if (dialog != null) {
            dialog.setCallback(this);
        }
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    public void connect() {
        shouldConnect = true;
        PreferenceUtils.setSignIn(context, true);

        if (!mResolvingError && !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        shouldConnect = false;
        PreferenceUtils.setSignIn(context, false);

        if (mGoogleApiClient.isConnected()) {
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    public void unlockAchievement(String id) {
        if (isConnected()) {
            Games.Achievements.unlock(mGoogleApiClient, id);
        }
    }

    public void incrementAchievement(String id) {
        if (isConnected()) {
            Games.Achievements.increment(mGoogleApiClient, id, 1);
        }
    }

    public void showAchievements() {
        context.startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
    }

    public void onStart() {
        if (!mResolvingError && shouldConnect) {
            mGoogleApiClient.connect();
        }
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mResolvingError) {
            if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(context, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    mGoogleApiClient.connect();
                }
            } else {
                GoogleServiceErrorDialog dialog = GoogleServiceErrorDialog.newInstance(result.getErrorCode(), REQUEST_RESOLVE_ERROR);
                dialog.setCallback(this);

                dialog.show(context.getFragmentManager(), GOOGLE_SERVICE_ERROR_DIALOG);
                mResolvingError = true;
                shouldConnect = false;
                PreferenceUtils.setSignIn(context, false);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == Activity.RESULT_OK) {

                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else {
                GoogleServiceErrorDialog.newInstance(context.getString(R.string.google_service_connection_unknown_error))
                        .show(context.getFragmentManager(), GOOGLE_SERVICE_ERROR_DIALOG);

                disconnect();
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
        outState.putBoolean(STATE_SHOULD_CONNECT, shouldConnect);
    }

    @Override
    public void onDismiss() {
        mResolvingError = false;
    }
}
