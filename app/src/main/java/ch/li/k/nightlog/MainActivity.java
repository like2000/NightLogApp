package ch.li.k.nightlog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.li.k.nightlog.monitor.MonitorFragment;

public class MainActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final MonitorFragment monitorFragment = new MonitorFragment();
    private final ControlsFragment controlsFragment = new ControlsFragment();
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private Fragment activeFragment;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        switch (item.getItemId()) {
            case R.id.bottom_nav_home:
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                return true;
            case R.id.bottom_nav_gallery:
                fragmentManager.beginTransaction().hide(activeFragment).show(controlsFragment).commit();
                activeFragment = controlsFragment;
                return true;
            case R.id.bottom_nav_list:
                fragmentManager.beginTransaction().hide(activeFragment).show(monitorFragment).commit();
                activeFragment = monitorFragment;
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager.beginTransaction().add(R.id.fragmentContainer, controlsFragment, "2").hide(controlsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, monitorFragment, "3").hide(monitorFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, homeFragment, "1").commit();
        activeFragment = homeFragment;
//        navigation.setSelectedItemId(R.id.bottom_nav_home);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}