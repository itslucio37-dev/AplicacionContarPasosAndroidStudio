package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_activity)

        val tvNombreActividad = findViewById<TextView>(R.id.tvNombreActividad)
        val tvEstadoActividad = findViewById<TextView>(R.id.tvEstadoActividad)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)


        val titulo = intent.getStringExtra("TITULO") ?: "Sin título"
        val isCompleted = intent.getBooleanExtra("ESTADO", false)

        tvNombreActividad.text = titulo

        if (isCompleted) {
            tvEstadoActividad.text = "Estado: ¡Completado!"
            tvEstadoActividad.setTextColor(android.graphics.Color.parseColor("#388E3C"))
        } else {
            tvEstadoActividad.text = "Estado: Pendiente"
            tvEstadoActividad.setTextColor(android.graphics.Color.parseColor("#757575"))
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }
}