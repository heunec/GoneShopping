package org.projects.shoppinglist;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by heunex on 13/05/16.
 */
public class MainProj extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }
}