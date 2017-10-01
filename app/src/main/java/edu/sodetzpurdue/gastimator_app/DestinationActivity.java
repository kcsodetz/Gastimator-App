package edu.sodetzpurdue.gastimator_app;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DestinationActivity extends AppCompatActivity {
    private EditText origin, destination;
    Button gastimate;
    String originString = "Origin";
    String destinationString = "Destination";
    String response = "empty";
    public final int NO_ORIGIN = 0;
    public final int NO_DESTINATION = 1;
    public final int SUCCESS = 2;
    public final int DEFAULT = 3;
    String time;
    double distance;
    GetDistance getDistance = new GetDistance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gastimate = (Button)findViewById(R.id.gastimateButton);
        gastimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                origin = (EditText)findViewById(R.id.editText);
                destination = (EditText)findViewById(R.id.editText2);
                originString = origin.getText().toString();
                destinationString = destination.getText().toString();
                if(originString.equals("")) {
                    messageToast(NO_ORIGIN);
                }
                else if(destinationString.equals("")) {
                    messageToast(NO_DESTINATION);
                }
                if(originString != null && destinationString != null &&
                        !originString.equalsIgnoreCase("Origin") && !destinationString.equalsIgnoreCase("Destination")) {
                        response = getDistance.googleMapsConnect(originString, destinationString);
                }
                if(response.contains("INVALID_REQUEST") || response.contains("ZERO_RESULTS")){
                    messageToast(DEFAULT);
                }
                else {
                    messageToast(SUCCESS);
                    //time = getDistance.parseTime(response);
                    distance = getDistance.parseDistance(response);
                    //System.out.println(time);
                    System.out.println(distance);
                }

            }
        });
    }

    /**
     * Handles toasts for missing inputs
     * @param input missing input
     */
    public void messageToast(int input){
        switch (input){
            case NO_ORIGIN:
                Toast.makeText(this, "You did not enter an Origin", Toast.LENGTH_SHORT).show();
                break;
            case NO_DESTINATION:
                Toast.makeText(this, "You did not enter a Destination", Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                break;
            case DEFAULT:
                Toast.makeText(this, "Please input values", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
