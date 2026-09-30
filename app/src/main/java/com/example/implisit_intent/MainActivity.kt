package com.example.implisit_intent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.TimeZone
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)
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

        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        btnSetAlarm.setOnClickListener {
            val _alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE,"COBA ALARM")
                putExtra(AlarmClock.EXTRA_HOUR,20)
                putExtra(AlarmClock.EXTRA_MINUTES,15)
                putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            }
            startActivity(_alarmIntent)
        }

        val btnSetTimer = findViewById<Button>(R.id.btnSetTimer)
        btnSetTimer.setOnClickListener {
            val _timerIntent = Intent(AlarmClock.ACTION_SET_TIMER).apply{
                putExtra(AlarmClock.EXTRA_MESSAGE,"COBA ALARM")
                putExtra(AlarmClock.EXTRA_LENGTH,20)
                putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            }
            startActivity(_timerIntent)
        }

        val btnOpenURL = findViewById<Button>(R.id.btnOpenURL)
        val _etURL = findViewById<EditText>(R.id.etURL)
        btnOpenURL.setOnClickListener {
            var _webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://"+_etURL.text.toString())
            )

            if (_webIntent.resolveActivity(packageManager) != null) {
                startActivity(_webIntent)
            } else{
                Toast.makeText(
                    this,
                    "Tidak ada Aplikasi Browser ditemukan",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val btnSetEvent = findViewById<Button>(R.id.btnSetEvent)
        btnSetEvent.setOnClickListener {
            val calender = Calendar.getInstance(TimeZone.getTimeZone("ASIA/Jakarta"))
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            val hour = calender.get(Calendar.HOUR_OF_DAY)
            val minute = calender.get(Calendar.MINUTE)

            val datePickerDialog =
                DatePickerDialog(this,{ _
                                         , selectedYear,
                                         selectedMonth,
                                         selectedDay ->

                    val timePickerDialog = TimePickerDialog(this, {
                        _,
                            selectedHour,
                            selectedMinute ->
                        val selectedDateTime = Calendar.getInstance().apply {
                            set(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMinute)
                        }
                        val endTime = selectedDateTime.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY,1)

                        val eventIntent = Intent(Intent.ACTION_INSERT).apply{
                            data= CalendarContract.Events.CONTENT_URI
                            putExtra(CalendarContract.Events.TITLE, "Meeting")
                            putExtra(CalendarContract.Events.EVENT_LOCATION, "Kantor")
                            putExtra(CalendarContract.Events.DESCRIPTION, "Deskripsi Meeting")
                            putExtra(CalendarContract.Events.ALL_DAY, false)
                            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,selectedDateTime.timeInMillis)
                            putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime.timeInMillis)
                        }
                        startActivity(eventIntent)

                    },hour,minute,true)

                    timePickerDialog.show()
                },year, month, day)

                datePickerDialog.show()
        }

        val btnGetPhoto=findViewById<Button>(R.id.btnGetPhoto)
        val _ivHasil=findViewById<ImageView>(R.id.ivHasil)
        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) {bitmap ->
            if (bitmap != null) {
                _ivHasil.setImageBitmap(bitmap)
            }
        }

        btnGetPhoto.setOnClickListener {
            cameraLauncher.launch(null)
        }

        val btnBukaMaps = findViewById<Button>(R.id.btnBukaMaps)
        btnBukaMaps.setOnClickListener {
            val _latitude= "-7.24611"
            val _longitude = "112.73750"
            val _labelTempat = "Tugu Pahlawan"

            val gmnIntentUri = Uri.parse("geo:$_latitude,$_longitude?q=$_latitude,$_longitude($_labelTempat)")

            var _mapIntent= Intent(Intent.ACTION_VIEW,gmnIntentUri).apply{
                setPackage("com.google.android.apps.maps")
            }
            if (_mapIntent.resolveActivity(packageManager) != null) {
                startActivity(_mapIntent)
            } else{
                Toast.makeText(
                    this,
                    "Aplikasi Google Maps tidak ditemukan",
                    Toast.LENGTH_SHORT).show()
                val _webUri= Uri.parse("https.//www.google.com/maps/search/?api=1&query=$_latitude,$_longitude")
                val _webIntent = Intent(
                    Intent.ACTION_VIEW,
                    _webUri)

                try {
                    startActivity(_webIntent)
                } catch (e2: Exception){
                    Toast.makeText(
                        this,
                        "Tidak ada aplikasi browser yang tersedia",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}