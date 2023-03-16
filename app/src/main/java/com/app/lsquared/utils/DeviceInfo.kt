package com.app.lsquared.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.text.SimpleDateFormat
import java.util.*


class DeviceInfo {

    companion object{

        // device id
        fun getDeviceId(ctx:Context,prefernce: MySharePrefernce):String{
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
                val megAvailable = bytesAvailable / 1000
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
            val megAvailable = bytesAvailable / 1000
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
//            try {
//                if (Build.VERSION.SDK_INT >= VERSION_CODES.P)return Build.ID
//                else return Build.SERIAL
//            }catch (ex : Exception){
//                return ""
//            }
            return "NA"
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

        fun getTime(ctx:Context) : String{
            val df = SimpleDateFormat("ddd MMM yyyy hh:mm:ss a", Locale.US)
            return df.format(Date())
        }

        fun bytesIntoHumanReadable(bytes: Long): String? {
            val megabyte = 1000
            val gigabyte = megabyte * 1000
            return ((bytes / gigabyte).toString())
        }

        fun getWifiStrength(ctx: Context): Int {
            val numberOfLevels = 5
            val wifiManager = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo: WifiInfo = wifiManager.getConnectionInfo()
            val level = WifiManager.calculateSignalLevel(wifiInfo.rssi, numberOfLevels)
            return level
        }

        fun getSerialNumber(): String? {
            var serialNumber: String?
            try {
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java)
                serialNumber = get.invoke(c, "gsm.sn1").toString()
                if (serialNumber == "") serialNumber = get.invoke(c, "ril.serialnumber").toString()
                if (serialNumber == "") serialNumber = get.invoke(c, "ro.serialno").toString()
                if (serialNumber == "") serialNumber = get.invoke(c, "sys.serialnumber").toString()
                if (serialNumber == "") serialNumber = Build.SERIAL

                // If none of the methods above worked
                if (serialNumber == "") serialNumber = null
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                serialNumber = null
            }
            return serialNumber
        }

    }
}