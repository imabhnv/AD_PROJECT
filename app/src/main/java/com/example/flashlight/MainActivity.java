package com.example.flashlight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private CameraManager cameraManager;
    private TextView textView;
    private float changevalue;
    private Sensor lightsensor;
    private String cameraid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.text);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraid = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        if(sensorManager .getDefaultSensor(Sensor.TYPE_LIGHT)  != null )
            lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        else
            Toast.makeText(this, "Light Sensor is not Available", Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        changevalue = event.values[0];
        textView.setText(String.valueOf(changevalue));

        if(changevalue<50) {
            try {
                cameraManager.setTorchMode(cameraid,true);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                cameraManager.setTorchMode(cameraid,false);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,lightsensor,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}