package com.navig.cambioimagenv3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity implements SensorEventListener {

    private ImageView imageView;
    private Drawable[] images;
    private int currentIndex = 0;

    // Sensor Manager y Sensor del acelerómetro
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // Constantes para definir los límites de sensibilidad del movimiento
    private static final float MOVEMENT_THRESHOLD = 5;
    private static final float MOVEMENT_LEFT_THRESHOLD = -MOVEMENT_THRESHOLD;

    // Variables para controlar la frecuencia de cambio de imágenes
    private long lastUpdate = 0;
    private static final long MIN_TIME_BETWEEN_UPDATES = 500; // 500 milisegundos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mantener la pantalla encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Inicialización de la matriz de imágenes
        images = new Drawable[]{
                getResources().getDrawable(R.drawable.acuario),
                getResources().getDrawable(R.drawable.aries),
                getResources().getDrawable(R.drawable.cancer),
                getResources().getDrawable(R.drawable.capricornio),
                getResources().getDrawable(R.drawable.escorpion),
                getResources().getDrawable(R.drawable.geminis),
                getResources().getDrawable(R.drawable.leo),
                getResources().getDrawable(R.drawable.libra),
                getResources().getDrawable(R.drawable.piscis),
                getResources().getDrawable(R.drawable.sagitario),
                getResources().getDrawable(R.drawable.tauro),
                getResources().getDrawable(R.drawable.virgo),
        };

        imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(images[currentIndex]);

        // Inicialización del SensorManager y el Sensor del acelerómetro
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // Verifica si el acelerómetro está disponible antes de registrarlo
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener la escucha del acelerómetro cuando la actividad esté en pausa
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Obtener los datos del acelerómetro
        float x = event.values[0];
        // Controlar la frecuencia de cambio de imágenes
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastUpdate < MIN_TIME_BETWEEN_UPDATES) {
            return;
        }
        lastUpdate = currentTime;
        // Cambiar la imagen según la dirección del movimiento
        if (x < MOVEMENT_LEFT_THRESHOLD) {
            // Retroceder una imagen si se mueve a la izquierda
            currentIndex = (currentIndex - 1 + images.length) % images.length;
            imageView.setImageDrawable(images[currentIndex]);
        } else if (x > MOVEMENT_THRESHOLD) {
            // Avanzar una imagen si se mueve a la derecha
            currentIndex = (currentIndex + 1) % images.length;
            imageView.setImageDrawable(images[currentIndex]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar esto para el acelerómetro
    }
}
