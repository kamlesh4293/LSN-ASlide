package com.app.lsquared.ui.activity

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat.PENDING_INTENT_FLAG_MUTABLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lsquared.databinding.ActivitySplashBinding
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.usbserialconnection.USBSerialConnector
import com.app.lsquared.utils.DataManager
import com.app.lsquared.utils.MySharePrefernce
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(){

    private lateinit var binding : ActivitySplashBinding

    @Inject
    lateinit var pref: MySharePrefernce

    // usb
    private var usbManager: UsbManager? = null
    private var serialPort: UsbSerialPort? = null

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            openMainActivity()
                        }
                    } else {
                        finish()
                    }
                }
            }
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = MySharePrefernce(this)
        DataManager.createReportFile(pref)

        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(usbReceiver, filter)

        usbManager = getSystemService(USB_SERVICE) as UsbManager
        var permissionIntent : PendingIntent? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION),
                PendingIntent.FLAG_MUTABLE)
        }else{
            permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
        }

        SystemClock.sleep(1000)
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers != null && !availableDrivers.isEmpty()) {
            val driver = availableDrivers[0]
            val ports = driver.ports
            if (!ports.isEmpty()) {
                serialPort = ports[0]
                usbManager!!.requestPermission(serialPort!!.getDriver().device, permissionIntent)
            }
        }else{
            openMainActivity()
        }

    }


    fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

}