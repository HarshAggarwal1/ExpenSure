package com.accenture.accpenture;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class GoogleSignInActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_google_sign_in);
    }

    private void config() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName("googleSignIn");
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        MaterialContainerTransform transform= new MaterialContainerTransform();
        transform.addTarget(android.R.id.content);
        transform.setDuration(300);
        getWindow().setSharedElementEnterTransition(transform);
        getWindow().setSharedElementExitTransition(transform);
    }

}
