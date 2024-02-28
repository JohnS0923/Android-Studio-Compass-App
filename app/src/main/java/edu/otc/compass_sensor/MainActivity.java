//Team: Louis Lathrop, Audrey Harmon, Johnny Sun

package edu.otc.compass_sensor;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //init variables
    private SensorManager sensorManager;
    private Sensor magnetometer;
    private Sensor accelerometer;
    private ImageView compassImageView;
    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];

    private float mheading;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set up sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //assigning sensors to variables
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //assigning views to variables
        compassImageView = findViewById(R.id.compass);
        heading = findViewById(R.id.heading);

    }
    @Override
    protected void onResume() {
        super.onResume();
        //register sensor listeners
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        //checking to see if sensor information has changed
        if (event.sensor == magnetometer) {
            //Assigning mag sensor output to an array
            System.arraycopy(event.values, 0, magnetometerValues, 0, 3);
        }
        else if (event.sensor == accelerometer) {
            //Assigning acc sensor output to an array
            System.arraycopy(event.values, 0, accelerometerValues, 0, 3);
        }
// Update your compass UI with the new azimuth value
        updateCompassUI();
    }
    private void updateCompassUI() {
        //setting up display movement values
        float[] rotationMatrix = new float[9];
        float[] orientationValues = new float[3];
        //Get the rotation matrix from the values of accelerometer and magnetometer
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues);
        //get the azimuth using get orientation method putting in values of rotation matrix and orientation
        float azimuthInRadians = SensorManager.getOrientation(rotationMatrix, orientationValues)[0];
        //convert the azimuth to degree
        float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);

        //set the rotation of compass base on the azimuth
        compassImageView.setRotation(-azimuthInDegrees);

        //if the azimuth is negative add 360 to make it positive
        if(azimuthInDegrees<0){
            mheading = azimuthInDegrees+360;
        }
        //otherwise keep it the same
        else{
            mheading = azimuthInDegrees;
        }
        //set up the heading degrees text
        heading.setText("Heading: "+mheading+" degrees");


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// Handle accuracy changes if needed
    }
}

