package com.app.lsquared.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.lsquared.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {

    ActivityWebBinding binding;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RelativeLayout rl = new RelativeLayout(this);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(1);
        rl.addView(frameLayout);

//        binding.webRelativeLayout.addView(rl);
//        getSupportFragmentManager().beginTransaction().add(frameLayout.getId(), new FragmentNews()).commit();

    }
}