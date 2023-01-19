package com.app.lsquared.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.lsquared.databinding.ActivityCodBinding
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.MainViewModel
import com.app.lsquared.ui.adapter.CODViewPagerAdapter
import com.app.lsquared.ui.fragment.FragmentPager
import com.app.lsquared.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class CODActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCodBinding
    private lateinit var viewModel: MainViewModel
    var job: Job? = null
    var temp = 0

    @Inject
    lateinit var myPerf: MySharePrefernce

    var yourCountDownTimer: CountDownTimer? = null


    // for screenshot
    var screenshot_handler: Handler = Handler()
    var screenshot_runnable: Runnable? = null
    // for temperature
    var temp_handler: Handler = Handler()
    var temp_runnable: Runnable? = null


    private val broadcastreceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXml()
        // broadcast reciver for temp
        var intentfilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(broadcastreceiver,intentfilter)
    }

    override fun onResume() {
        super.onResume()
        initCountDownTimer()
        viewModel.internet = isConnected
        viewModel.is_device_registered = true
        Log.d("TAG", "onResume: ss delay ${myPerf.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)}")
        viewModel.screen_delay = myPerf.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)

        // screen shot
        screenshot_handler.postDelayed(Runnable {
            screenshot_handler.postDelayed(
                screenshot_runnable!!,
                viewModel.screen_delay * 1000.toLong()
            )
            var file = ImageUtil.screenshot(binding.consCodRoot, "Screen_final_" + Utility.getCurrentdate())
            viewModel.submitScreenShot(
                Utility.getScreenshotJson(DeviceInfo.getDeviceId(this),
                    Utility.getFileToByte(file?.absolutePath)
                )
            )
        }.also { screenshot_runnable = it }, viewModel.screen_delay * 1000.toLong())

        // temp handler
        temp_handler.postDelayed(kotlinx.coroutines.Runnable {
            temp_handler.postDelayed(temp_runnable!!, viewModel.temp_delay.toLong())
            viewModel.updateTempratureData(temp.toString())
        }.also { temp_runnable = it }, viewModel.temp_delay.toLong())

    }

    private fun initXml() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = flags


        binding = ActivityCodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCountDownTimer()
        // init view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // Initializing the ViewPagerAdapter
        val adapter = CODViewPagerAdapter(supportFragmentManager)

        var cod_item_list = DataParsing.getCodItems(myPerf)
        if(cod_item_list.size>0){
            for(i in 0..cod_item_list.size-1){
                if(cod_item_list[i].cat!=null && cod_item_list[i].cat.size>0){
                    adapter.addFragment(FragmentPager(cod_item_list[i]),cod_item_list[i].name!!)
                }
            }
        }

        // Adding the Adapter to the ViewPager
        binding.viewPager.adapter = adapter

        // bind the viewPager with the TabLayout.
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onPause() {
        super.onPause()
        screenshot_handler.removeCallbacks(screenshot_runnable!!)
    }

    fun initCountDownTimer() {
        var ideal_time = myPerf.getIntData(MySharePrefernce.KEY_COD_IDEAL_TIME)*60
        if(ideal_time==0) return

        if (yourCountDownTimer != null) yourCountDownTimer?.cancel()
        yourCountDownTimer = object : CountDownTimer(ideal_time.toLong() * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                finish()
            }
        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        if(job!=null)job!!.cancel()
    }

    fun stopCounter() {
        if (yourCountDownTimer != null) yourCountDownTimer!!.cancel()
    }


}