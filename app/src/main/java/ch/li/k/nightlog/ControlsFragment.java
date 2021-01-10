package ch.li.k.nightlog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import ch.li.k.nightlog.databinding.ControlsFragmentBinding;

public class ControlsFragment extends Fragment implements View.OnFocusChangeListener {

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

        viewModel.getStop().observe(getViewLifecycleOwner(), localDateTime -> viewModel.computeStop());

        binding.setLifecycleOwner(this);
        binding.setModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        viewModel.getDate().observe(getViewLifecycleOwner(), new Observer<LocalDate>() {
//            @Override
//            public void onChanged(LocalDate localDate) {
//                viewModel
//            }
//        });
//    }

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

        binding.textViewDate.setOnFocusChangeListener((view, b) -> {
            LocalDate value = null;
            viewModel.getDateActive().setValue(b);
            try {
                value = LocalDate.parse(((EditText) view).getText(), DateTimeFormatter.ofPattern("EEE dd. MMM yyyy").withLocale(Locale.GERMANY));
                System.out.println("Hello running! " + value.toString());
                viewModel.getDate().setValue(value);
            } catch (DateTimeParseException e) {
                Log.w("Warning", "Bad parse string!");
                viewModel.getDate().setValue(null);
                e.printStackTrace();
            }
        });
        binding.textViewStop.setOnFocusChangeListener((view, b) -> {
            LocalDateTime value = null;
            viewModel.getStopActive().setValue(b);
            try {
                Editable text = ((EditText) view).getText();
                LocalTime time = LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMANY));
                value = viewModel.getDate().getValue().atTime(time);
                viewModel.getStop().setValue(value);
            } catch (DateTimeParseException e) {
                Log.w("Warning", "Bad parse string!");
                viewModel.getStop().setValue(null);
                e.printStackTrace();
            }

        });
        binding.textViewStart.setOnFocusChangeListener((view, b) -> {
            LocalDateTime value = null;
            viewModel.getStartActive().setValue(b);
            try {
                Editable text = ((EditText) view).getText();
                LocalTime time = LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMANY));
                value = viewModel.getDate().getValue().atTime(time);
                viewModel.getStart().setValue(value);
            } catch (DateTimeParseException e) {
                Log.w("Warning", "Bad parse string!");
                viewModel.getStart().setValue(null);
                e.printStackTrace();
            }
        });

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

    @Override
    public void onFocusChange(View view, boolean b) {
        viewModel.getDateActive().setValue(b);
    }
}