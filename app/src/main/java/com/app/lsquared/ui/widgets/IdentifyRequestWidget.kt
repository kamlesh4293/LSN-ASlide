package com.app.lsquared.ui.widgets

import android.content.Context
import android.os.Build
import android.util.Log
import com.app.lsquared.BuildConfig
import com.app.lsquared.databinding.ActivityMainMultifameBinding
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DataParsing
import com.app.lsquared.utils.DeviceInfo
import com.app.lsquared.utils.MySharePrefernce
import kotlinx.android.synthetic.main.activity_main_multifame.view.*

class IdentifyRequestWidget {

    companion object{

        fun setIdentifyRequest(binding: ActivityMainMultifameBinding, pref: MySharePrefernce,ctx:Context
        , internet:Boolean,temp:String, device_reg:Boolean) {

            var device = DataParsing.getDevice(pref)

            if(device_reg){
                binding.tvMainIdentifyName.text = "Name:  ${device?.name}"

                Log.d("TAG", "setIdentifyRequest: ${device?.server}")
                if(device?.server!!.contains("-")){
                    var string_arry = device?.server?.split("-")
                    var stringBuilder = StringBuilder()
                    for(i in 0..string_arry!!.size-1){
                        Log.d("TAG", "setIdentifyRequest array: ${string_arry[i]}")
                        if(i!=0) stringBuilder.append(string_arry[i])
                    }
                    binding.tvMainIdentifyServer.text = "Server:  $stringBuilder"
                }else binding.tvMainIdentifyServer.text = "Server:  ${device?.server}"

            }else{
                binding.tvMainIdentifyName.text = "Name:  "
                binding.tvMainIdentifyServer.text = "Server:  "
            }
            binding.tvMainIdentifyId.text = "Id:  ${DeviceInfo.getDeviceId(ctx,pref)}"
            binding.tvMainIdentifyEnv.text = "Environment:  ${Constant.ENVIRONMENT}"

            binding.tvMainIdentifyType.text = "Type:  Android"
            binding.tvMainIdentifyModel.text = "Model:  ${DeviceInfo.getModelName()}"
            binding.tvMainIdentifyTizen.text = "Android:  ${DeviceInfo.getDeviceVersion()}"
            binding.tvMainIdentifySerial.text = "Serial:  ${DeviceInfo.getSerial()}"
            binding.tvMainIdentifyLss.text = "L Squared Slide:  ${BuildConfig.VERSION_NAME}"
            binding.tvMainIdentifyIp.text = "IP:  ${DeviceInfo.getLocalIpAddress()} | ${DeviceInfo.getWifiMacAddress(ctx)}"

            var total = DeviceInfo.bytesIntoHumanReadable(DeviceInfo.getTotalDiscSize())!!.toInt()
            var used = DeviceInfo.bytesIntoHumanReadable(DeviceInfo.getUsedDiscSize())!!.toInt()

            binding.tvMainIdentifyStorage.text = "Available Storage:  ${total-used} / $total (GB)"
            binding.tvMainIdentifyTime.text = "Time:  ${DeviceInfo.getTime(ctx)}"
            binding.tvMainIdentifyTemp.text = "Temperature:  $temp °c"

            var connection = if(internet) "Connected" else "Disconnected"
            var status = if(internet) "Online" else "Offline"
            binding.tvMainIdentifyConnType.text = "Connection Type:  ${DeviceInfo.getConnectedNetworkType(ctx)}"
            binding.tvMainIdentifyWifi.text = "WiFi Strength:  ${DeviceInfo.getWifiStrength(ctx)}"
            binding.tvMainIdentifyGateway.text = "Gateway:  $connection"
            binding.tvMainIdentifyInternet.text = "Internet:  $connection"
            binding.tvMainIdentifyStatus.text = "Status:  $status"

        }

    }
}