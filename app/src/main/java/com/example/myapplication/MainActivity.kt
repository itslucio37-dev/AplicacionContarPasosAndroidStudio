package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ActivityAdapter
import com.example.myapplication.models.Activity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var contador = 0;
    private var pasosIniciales = -1f
    private var pasosTotalesSensor = 0f
    private lateinit var sensorManager: SensorManager;
    private lateinit var tvContador : TextView;
    private var sensorPasos: Sensor? = null;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvContador = findViewById(R.id.tvContador);
        val btnSumar = findViewById<Button>(R.id.btnSumar);
        val btnReiniciarPasos = findViewById<Button>(R.id.btnReiniciarPasos);
        val btnDetalle = findViewById<Button>(R.id.btnDetalle);



        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 100)
        }else{
            configurarSensor();
        }


        val activityList = listOf(
            Activity("Natacion"),
            Activity("Rutina de Fuerza"),
            Activity("Sesion de Box"),
            Activity("Desayuno"),
            Activity("Comida")

        )
        val rvActividades = findViewById<RecyclerView>(R.id.rvActividades)
        rvActividades.layoutManager = LinearLayoutManager(this)
        rvActividades.adapter = ActivityAdapter(activityList) {
            actividadSeleccionada ->
            // Este bloque se ejecuta cuando se toca la tarjeta
            val intent = Intent(this, ActivityDetail::class.java)
            intent.putExtra("TITULO", actividadSeleccionada.title)
            intent.putExtra("ESTADO", actividadSeleccionada.isCompleted)
            startActivity(intent)
        }


        btnSumar.setOnClickListener{
            contador++;

            tvContador.text = "Numero de pasos: $contador";
        }

        btnReiniciarPasos.setOnClickListener{
            if (pasosTotalesSensor > 0f) {
                pasosIniciales = pasosTotalesSensor
                contador = 0
                tvContador.text = "Numero de pasos: $contador"
            } else {
                contador = 0
                tvContador.text = "Numero de pasos: $contador"
            }
        }

        //Navegar a otra pantalla de detalle
        btnDetalle.setOnClickListener{

            val intent = Intent(this, Detail::class.java);

            intent.putExtra("PASOS", contador);

            startActivity(intent);
        }

    }

    private fun configurarSensor(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        sensorPasos = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(sensorPasos == null)
        {
            tvContador.text = "Error al configurar el sensor de pasos";
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                configurarSensor();
            } else{
                tvContador.text = "Permiso de sensor no concedido";
            }
        }
    }

    override fun onResume(){
        super.onResume()
        sensorPasos?.let{
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause(){
        super.onPause();
        if (::sensorManager.isInitialized) {
            sensorManager.unregisterListener(this)
        }
        //sensorManager.unregisterListener(this);
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            pasosTotalesSensor = event.values[0]


            if (pasosIniciales < 0f) {
                pasosIniciales = pasosTotalesSensor
            }


            contador = (pasosTotalesSensor - pasosIniciales).toInt()
            tvContador.text = "Numero de pasos: $contador"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    /*override fun onSensorChanged(event: SensorEvents){

    }*/

    /*override fun onAccuracyChange()
    {

    }*/
}