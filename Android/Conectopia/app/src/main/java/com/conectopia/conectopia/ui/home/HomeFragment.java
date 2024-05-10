package com.conectopia.conectopia.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    int count = 0;

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        this.homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        root.findViewById(
                R.id.button
        ).setOnClickListener(v -> plusOne());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void plusOne() {
        View root = binding.getRoot();

        this.count++;
        // write the count to the ui MytextView
        this.homeViewModel.mText.setValue(String.valueOf(this.count));

    }
}