package ch.li.k.nightlog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ch.li.k.nightlog.databinding.ControlsFragmentBinding;

public class ControlsFragment extends Fragment {

    private StatsViewModel viewModel;
    private ControlsFragmentBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_controls, container, false);

        binding = ControlsFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

//        View view = requireActivity().getCurrentFocus();
        ConstraintLayout view = binding.ConstraintLayout;
//        View view = (View) fragment.getView().getRootView().getWindowToken();

        if (hidden) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setListeners() {
        binding.ConstraintLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        binding.textViewDate.setOnFocusChangeListener((view, b) -> viewModel.getDateActive().setValue(b));
        binding.textViewStop.setOnFocusChangeListener((view, b) -> viewModel.getStopActive().setValue(b));
        binding.textViewStart.setOnFocusChangeListener((view, b) -> viewModel.getStartActive().setValue(b));

        binding.buttonSave.setOnClickListener(view -> addEntry());
    }

    public void addEntry() {
        try {
            viewModel.addEntry();
            Toast.makeText(getContext(), "New entry added!", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException exc) {
            Toast.makeText(getContext(), "Failed to add entry! Are all fields above defined?", Toast.LENGTH_SHORT).show();
        }
    }
}