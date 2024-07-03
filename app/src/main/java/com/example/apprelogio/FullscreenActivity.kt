package com.example.apprelogio

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.apprelogio.databinding.ActivityFullscreenBinding
import java.util.Calendar

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeTask = object:Runnable {
        override fun run(){
            configHour()
            handler.postDelayed(this, 1000)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        removeStatusBar()
        nivelBateria()
        enableBatteryLevel()
        handler.post(updateTimeTask)
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeTask)
    }



    fun nivelBateria(){
        val bateriaReceiver:BroadcastReceiver = object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent !=null){
                    val nivel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0)
                    binding.txtLvlBateria.setText(nivel.toString() + "%")
                }
            }
        }
        registerReceiver(bateriaReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
    fun enableBatteryLevel(){
        binding.cbLvlBattery.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.txtLvlBateria.visibility = View.VISIBLE
            } else {
                binding.txtLvlBateria.visibility = View.GONE
            }
        }
    }
    fun removeStatusBar(){
        //Remover Statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        //App funciona sempre com a tela ligada
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun configHour(){
        val rightNow =Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val currentSecond = rightNow.get(Calendar.SECOND)

        val formatHour:String = "${currentHour.toString()}:${currentMinute.toString()}"
        binding.clockHorasMin.setText(formatHour)
        binding.clockSegundos.setText(currentSecond.toString())
    }


}