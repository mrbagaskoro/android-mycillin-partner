package com.mycillin.partner.modul.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

public class FireBaseIDServices extends FirebaseInstanceIdService {
    private static final String TAG = "firebase";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.tag(TAG).d("Refreshed token: %s", refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        FirebaseManager firebaseManager = new FirebaseManager(getApplicationContext());
        firebaseManager.setFirebaseToken(token);
    }
}
