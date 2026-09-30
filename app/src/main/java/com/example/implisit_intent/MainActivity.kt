package com.example.implisit_intent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnKirimPesan.setOnClickListener {
            val _sendIntent = Intent().apply{
                action= Intent.ACTION_SEND
                putExtra("address","0811234")
                putExtra("sms_body","ISI SMS")
                type = "text/plain"
            }

            if (_sendIntent.resolveActivity(packageManager) !=null) {
                startActivity(Intent.createChooser(_sendIntent,"PILIH APLIKASI"))
            }
        }
    }
}