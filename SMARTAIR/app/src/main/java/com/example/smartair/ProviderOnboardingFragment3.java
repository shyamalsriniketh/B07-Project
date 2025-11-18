package com.example.smartair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ProviderOnboardingFragment3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.provider_onboarding3, container, false);
        Button nextButton = view.findViewById(R.id.button);

        nextButton.setOnClickListener(v -> {
            if (getActivity() instanceof OnboardingActivity) {
                OnboardingActivity activity = (OnboardingActivity) getActivity();
                activity.completeOnboarding();
            }
        });

        return view;
    }
}


