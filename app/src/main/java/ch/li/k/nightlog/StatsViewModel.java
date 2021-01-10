package ch.li.k.nightlog;

import android.app.Application;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StatsViewModel extends AndroidViewModel {

    private final MutableLiveData<String> total = new MutableLiveData<String>();
    private final MutableLiveData<LocalDate> date = new MutableLiveData<LocalDate>();

    private final MutableLiveData<LocalDateTime> stop = new MutableLiveData<LocalDateTime>();
    private final MutableLiveData<LocalDateTime> start = new MutableLiveData<LocalDateTime>();
    private final MutableLiveData<Boolean> dateActive = new MutableLiveData<Boolean>();
    private final MutableLiveData<Boolean> stopActive = new MutableLiveData<Boolean>();
    private final MutableLiveData<Boolean> startActive = new MutableLiveData<Boolean>();
    private final MutableLiveData<List<StatsModel>> itemsList = new MutableLiveData<List<StatsModel>>();

    public StatsViewModel(@NonNull Application application) {
        super(application);

        getDateActive().setValue(false);
        getStopActive().setValue(false);
        getStartActive().setValue(false);
    }

    public static String[] toStringArray(StatsViewModel viewModel) {
        return new String[]{
                viewModel.getDate().getValue().format(DateTimeFormatter.ofPattern("EEE dd. MMM yyyy").withLocale(Locale.GERMANY)),
                viewModel.getStart().getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                viewModel.getStop().getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                viewModel.getTotal().getValue()
        };
    }

    public static String[] toStringArray(StatsModel model) {
        return new String[]{
                model.getDate(),
                model.getStart(),
                model.getStop(),
                model.getTotal()
        };
    }

    public static List<String[]> toStringArrayList(StatsViewModel viewModel) {
        ArrayList<String[]> data = new ArrayList<String[]>();

        for (StatsModel row : Objects.requireNonNull(viewModel.getItemsList().getValue())) {
            data.add(toStringArray(row));
        }

        return data;
    }


    public MutableLiveData<String> getTotal() {
        return total;
    }

    //    @InverseBindingAdapter(attribute = "date")
    public MutableLiveData<LocalDate> getDate() {
        return date;
    }

    //    @BindingAdapter("date")
    public void setDate(EditText view, String date) {
        System.out.println("Print date: " + date);
        view.setText(date);
    }


    public MutableLiveData<LocalDateTime> getStart() {
        return start;
    }

    public MutableLiveData<LocalDateTime> getStop() {
        return stop;
    }

    public MutableLiveData<Boolean> getDateActive() {
        return dateActive;
    }

    public MutableLiveData<Boolean> getStopActive() {
        return stopActive;
    }

    public MutableLiveData<Boolean> getStartActive() {
        return startActive;
    }

    public MutableLiveData<List<StatsModel>> getItemsList() {
        return itemsList;
    }

    public void computeStart() {
        getStart().setValue(LocalDateTime.now());
    }

    public void computeStop() {
        getStop().setValue(LocalDateTime.now());

        LocalDateTime start = getStart().getValue();
        LocalDateTime stop = getStop().getValue();
        try {
            Duration duration = Duration.between(start, stop);
            getTotal().setValue(String.format(Locale.GERMANY, "%d:%02d:%02d",
                    duration.getSeconds() / 3600, (duration.getSeconds() % 3600) / 60, (duration.getSeconds() % 60)));
        } catch (NullPointerException err) {
            Thread.currentThread().getStackTrace();
        }
    }

    public void newDate() {
        Locale.setDefault(Locale.GERMANY);
        LocalTime time = LocalTime.now();
        if (time.getHour() < 5)
            this.getDate().setValue(LocalDate.now().minusDays(1));
        else
            this.getDate().setValue(LocalDate.now());
    }

    public void newTime() {
        if (this.getDate().getValue() == null)
            Toast.makeText(getApplication().getApplicationContext(), "You must first set the date!", Toast.LENGTH_SHORT).show();
        else {
            if (getStartActive().getValue())
                computeStart();
            else if (getStopActive().getValue())
                computeStop();
        }
    }

    public void addEntry() {
        List<StatsModel> data = getItemsList().getValue();

        data.add(new StatsModel(
                getDate().getValue().format(DateTimeFormatter.ofPattern("EEE dd. MMM yyyy").withLocale(Locale.GERMANY)),
                getStart().getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                getStop().getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                getTotal().getValue()));

        getItemsList().setValue(data);
    }

    public void clearField() {
        if (getDate().getValue() != null)
            if (getDateActive().getValue())
                getDate().setValue(null);
        if (getStop().getValue() != null)
            if (getStartActive().getValue())
                getStart().setValue(null);
        if (getStart().getValue() != null)
            if (getStopActive().getValue())
                getStop().setValue(null);
    }

    public void clearAll() {
        getDate().setValue(null);
        getStop().setValue(null);
        getStart().setValue(null);
        getTotal().setValue(null);
    }
}
