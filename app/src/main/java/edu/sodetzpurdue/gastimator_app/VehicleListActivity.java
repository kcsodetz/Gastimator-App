package edu.sodetzpurdue.gastimator_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lists the current saved vehicles
 *
 * @author Ken Sodetz
 * @since 9/20/2017
 */
public class VehicleListActivity extends AppCompatActivity {
    /**
     * onCreate Method
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        final SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor prefsEditor = mPrefs.edit();
        final Gson gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_car);
        this.setTitle("Choose or Add a Vehicle");
        ListView lv = (ListView) findViewById(R.id.listView);
        final Intent intent = getIntent();
        if (intent.hasExtra("car")) {
            Car car = (Car)intent.getSerializableExtra("car");
            System.out.println(car.getModel());
            String json = gson.toJson(car);
            prefsEditor.putString(car.getYear()+" "+car.getMake() + " " + car.getModel(), json);
            prefsEditor.apply();
        }

        final List<String> carList = new ArrayList<>();
        final Map<String,?> keys = mPrefs.getAll();
        for (Map.Entry<String,?> entry : keys.entrySet()) {
            carList.add(entry.getKey());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<> (this,
                android.R.layout.simple_expandable_list_item_1, carList);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent intentNext = new Intent(VehicleListActivity.this, DestinationActivity.class);
                String key = (String) adapter.getAdapter().getItem(position);
                String json = mPrefs.getString(key, "");
                Car car = gson.fromJson(json, Car.class);
                intentNext.putExtra("car", car);
                startActivity(intentNext);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long arg3) {
                vibrator.vibrate(50);
                prefsEditor.remove(parent.getItemAtPosition(position).toString());
                prefsEditor.commit();
                carList.remove(removeElement(parent.getItemAtPosition(position).toString(),
                        carList));
                arrayAdapter.notifyDataSetChanged();
                toastMessage();
                return true;
            }
        });
    }

    /**
     * Removes from List based on given element
     * @param element element to be removed
     * @param items list to remove from
     * @return -1 if unsuccessful
     */
    public static int removeElement(String element, List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(element))
                return i;
        }
        return -1;
    }

    /**
     * Handler for the press of the FAB
     * @param view current view
     */
    public void pressedFAB(View view) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    /**
     * Toast Message for "Deleted"
     */
    private void toastMessage() {
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }
}
