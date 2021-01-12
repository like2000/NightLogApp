package ch.li.k.nightlog.monitor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.li.k.lib.DataProvider;
import ch.li.k.nightlog.R;
import ch.li.k.nightlog.databinding.FragmentMonitorBinding;
import ch.li.k.nightlog.stats.StatsModel;
import ch.li.k.nightlog.stats.StatsViewModel;

public class MonitorFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static String DIRECTORY;
    private static String FILENAME = "data.csv";

    private final List<StatsModel> modelData = new ArrayList<>();
    private final MonitorAdapter adapter = new MonitorAdapter();

    private FragmentMonitorBinding binding;
    private RecyclerView recyclerView;
    private StatsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_monitor, container, false);
        FILENAME = getResources().getString(R.string.app_name).replaceAll(" ", "_").toLowerCase() + "_" + FILENAME;
        if (Build.VERSION.SDK_INT < 29) {
//            DIRECTORY = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            DIRECTORY = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).toString();
        } else throw new RuntimeException("Incompatible SDK version >= 29");

        binding = FragmentMonitorBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);
        viewModel.getItemsList().observe(getViewLifecycleOwner(), adapter::setMonitorItemsList);

        binding.setLifecycleOwner(this);
        binding.setModel(viewModel);

        try {
            readFileInternal();
        } catch (IOException e) {
            initFileStream();
            initFile();
        }
    }

    void setListeners() {
        binding.buttonSaveList.setOnClickListener(view -> writeFileInternal());
        binding.buttonDelete.setOnClickListener(view -> deleteEntry());
    }

    void initFileStream() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    void initFile() {
        try {
            DataProvider.initFile(DIRECTORY + "/" + FILENAME);
            Log.i("Info", "Created new file: " + DIRECTORY + "/" + FILENAME);
        } catch (IOException exc) {
            Toast.makeText(getContext(), "Failed to initialize file: " + DIRECTORY + "/" + FILENAME, Toast.LENGTH_SHORT).show();
            exc.fillInStackTrace();
        }
    }

    void readFileInternal() throws IOException {
        List<String[]> data = DataProvider.readFile(DIRECTORY + "/" + FILENAME);

        for (String[] d : data) {
            modelData.add(new StatsModel(d[0], d[1], d[2], d[3]));
        }
        viewModel.getItemsList().setValue(modelData);

        Log.i("Info", "Read file from: " + DIRECTORY + "/" + FILENAME);
        Log.i("Info", "Successfully read file data: " + modelData.toString());
    }

    void writeFileInternal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Add entry")
                .setMessage("Are you sure you want to save the current list to file?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with overwrite operation
                        try {
                            Log.i("Info", DIRECTORY + "/" + FILENAME);
                            List<String[]> data = StatsViewModel.toStringArrayList(viewModel);
                            DataProvider.writeFile(DIRECTORY + "/" + FILENAME, data);
                            Toast.makeText(getContext(), "File saved to: " + DIRECTORY, Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Successfully saved file to: " + DIRECTORY, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    void deleteEntry() {
        Log.i("Info", String.valueOf(adapter.selectedRow));

        try {
            StatsModel item = viewModel.getItemsList().getValue().remove(adapter.selectedRow);
            adapter.notifyItemRemoved(adapter.selectedRow);
            adapter.selectedRow = RecyclerView.NO_POSITION;
        } catch (ArrayIndexOutOfBoundsException exc) {
            exc.printStackTrace();
        }
    }
}