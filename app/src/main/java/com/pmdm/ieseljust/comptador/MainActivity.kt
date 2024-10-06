package com.pmdm.ieseljust.comptador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var comptador=0

    private var TAG : String = "DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencia al TextView
        val textViewContador=findViewById<TextView>(R.id.textViewComptador)

        // Inicialitzem el TextView amb el comptador a 0
        textViewContador.text=comptador.toString() // Estem fent una assignacio directament o accedinta algun metode?

        // Referencia al botón
        val btAdd=findViewById<Button>(R.id.btAdd)
        val btMenos = findViewById<Button>(R.id.btResta)
        val btReset = findViewById<Button>(R.id.btReset)

        // Asociaciamos una expresióin lambda como
        // respuesta (callback) al evento Clic sobre
        // el botón
        btAdd.setOnClickListener {
            comptador++
            textViewContador.text=comptador.toString()
        }

        btMenos.setOnClickListener {
            comptador--
            textViewContador.text=comptador.toString()
        }

        btReset.setOnClickListener {
            comptador = 0
            textViewContador.text=comptador.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("contador", comptador)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        comptador = savedInstanceState.getInt("contador")

        val textViewContador=findViewById<TextView>(R.id.textViewComptador)
        textViewContador.text=comptador.toString()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Al mètode onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Al mètode onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Al mètode onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Al mètode onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "Al mètode onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Al mètode onDestroy")
    }
}