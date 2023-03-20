package com.app.lsquared.ui.activity

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lsquared.databinding.ActivitySplashBinding
import com.app.lsquared.utils.MySharePrefernce
import com.app.lsquared.utils.StringToByteArray
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


//https://stackoverflow.com/questions/11011515/how-to-read-and-write-data-to-com-serial-ports-in-android

@AndroidEntryPoint
class UsbPortActivity : AppCompatActivity(){

    private lateinit var binding : ActivitySplashBinding

    @Inject
    lateinit var pref: MySharePrefernce

    private val deviceFound: UsbDevice? = null
    private val usbDeviceConnection: UsbDeviceConnection? = null
    private val usbInterfaceFound: UsbInterface? = null
    private val endpointOut: UsbEndpoint? = null
    private val endpointIn: UsbEndpoint? = null

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    private val usbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    showToast("onrc - ${device?.deviceName}")
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            //call method to set up device communication
                        }
                    } else {
                        Log.d("TAG", "permission denied for device $device")
                    }
                }
            }
        }
    }

    lateinit var device: UsbDevice
    lateinit var usbManager: UsbManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(usbReceiver, filter)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
//        usbManager.requestPermission(device, permissionIntent)


        binding.logo.setOnClickListener {
//            connectDevice()
            connectUsbDevice()
        }
    }

    private fun connectUsbDevice() {
        val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
        showToast("device name = $device")
        val deviceList = usbManager.getDeviceList()
        deviceList.values.forEach { device ->
            showToast("device name from list = ${device.deviceName}")
        }
    }

    fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }



    fun connectDevice(){

        var timeout = 5000
        var byteArray = StringToByteArray.hexStringToByteArray()

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val devices: Map<String, UsbDevice> = usbManager!!.getDeviceList()
        var builder = StringBuilder()
        var dev_name = ""
        for (device in devices){
            dev_name = device.key
            builder.append("${device.value.deviceName} \n")
            showToast(dev_name)
        }

        var usbDevice = devices.get(dev_name)
//        if(usbDevice!=null) showToast("Device not null")
        var usbconnection = usbManager?.openDevice(usbDevice)
        if(usbconnection==null){
            showToast("connection is null")
        }
        var usbEndpoint = usbDevice?.getInterface(0)?.getEndpoint(0)

        usbconnection?.claimInterface(usbDevice?.getInterface(0),true)
        usbconnection?.bulkTransfer(usbEndpoint,byteArray,byteArray.size,timeout)
    }

}