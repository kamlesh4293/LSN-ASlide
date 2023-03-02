package com.app.lsquared.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import android.os.Build.VERSION_CODES
import java.lang.Exception
import android.util.DisplayMetrics
import android.util.Log
import java.lang.StringBuilder
import android.net.wifi.WifiManager
import android.text.format.Formatter
import com.app.lsquared.BuildConfig
import org.json.JSONObject

class DeviceInfo {

    companion object{

        // device id
        fun getDeviceId(ctx:Context,prefernce: MySharePrefernce):String{
            var id = prefernce.getStringData(MySharePrefernce.KEY_DEVICE_REGISTERED_ID)
            return Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
        }

        // device id
        fun getDeviceIdFromDevice(ctx:Context):String{
            return Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
        }

        // Screen resolution
        fun getDeviceResolution(activity: Activity):String{
            var x_sign = "x"
            return "${getScreenWidth(activity)}$x_sign${getScreenHeight(activity)}"
        }

        fun getScreenWidth(ctx:Context):Int{
            return (ctx as Activity).resources.displayMetrics.widthPixels
        }

        fun getScreenHeight(ctx:Context):Int{
            var height = getNavigationBarHeight(ctx)
            Log.d("TAG : ", " getScreenHeight: $height")
            Log.d("TAG : ", " getScreenHeight: total ${(ctx as Activity).resources.displayMetrics.heightPixels + height}")
            return (ctx as Activity).resources.displayMetrics.heightPixels + height
        }

        private fun getNavigationBarHeight(ctx:Context): Int {
            val resources = ctx.resources
            val resName = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                "navigation_bar_height"
            } else {
                "navigation_bar_height_landscape"
            }
            val id: Int = resources.getIdentifier(resName, "dimen", "android")
            return if (id > 0) {
                resources.getDimensionPixelSize(id)
            } else {
                0
            }
        }

        // device name
        fun getDeviceName():String{
            return Build.MODEL;
        }

        // device ip address
        fun getLocalIpAddress(): String {
            try {
                val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf: NetworkInterface = en.nextElement()
                    val enumIpAddr: Enumeration<InetAddress> = intf.getInetAddresses()
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress: InetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress() && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress()
                        }
                    }
                }
            } catch (ex: SocketException) {
                ex.printStackTrace()
            }
            return ""
        }

        // total internal memory
        fun getTotalDiscSize(): Long {
            try{
                val stat = StatFs(Environment.getExternalStorageDirectory().path)
                val bytesAvailable: Long = if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                    stat.blockSizeLong * stat.blockCountLong
                } else {
                    return 0
                }
                val megAvailable = bytesAvailable / 1024
                return megAvailable
            }catch (ex:RuntimeException){
                return 0
            }
        }

        // get available internal memory
        fun getUsedDiscSize(): Long {
            val stat = StatFs(Environment.getExternalStorageDirectory().path)
            val bytesAvailable: Long
            if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
            }else{
                return 0
            }
            val megAvailable = bytesAvailable / 1024
            return megAvailable
        }

        //  get RAM total size
        fun getTotalRAMSize(ctx: Context) : Long{
            val actManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)
            val availMemory = memInfo.availMem.toDouble()
            val totalMemory= memInfo.totalMem
            return totalMemory
        }

        //  get RAM  size
        fun getUsedRAMSize(ctx: Context) : Long{
            val actManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)
            val availMemory = memInfo.availMem
            return availMemory
        }

        fun getSerial(): String{
            try {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.P)return Build.getSerial()
                else return Build.SERIAL
            }catch (ex : Exception){
                return ""
            }
        }

        fun getModelName(): String {
            return Build.DEVICE
        }

        fun getConnectedNetworkType(context: Context): String {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return networkInfo!!.typeName
        }

        fun getDeviceVersion() : String{
            return Build.VERSION.RELEASE.toString()
        }

        fun getWifiMacAddress(ctx:Context) : String{
            var ip_address = ""
            try {
                val wifiManager = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wInfo = wifiManager.connectionInfo
                ip_address = Formatter.formatIpAddress(wInfo.ipAddress)
                return ip_address
            }catch (ex: Exception){
                return ip_address
            }
        }

    }
}