package com.app.lsquared.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.app.lsquared.databinding.ActivityCodBinding
import com.app.lsquared.model.CodItem
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.MainViewModel
import com.app.lsquared.ui.adapter.CODViewPagerAdapter
import com.app.lsquared.ui.adapter.CodTabAdapter
import com.app.lsquared.ui.fragment.FragmentPager
import com.app.lsquared.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cod.view.*
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class CODActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCodBinding
    private lateinit var viewModel: MainViewModel
    var job: Job? = null

    @Inject
    lateinit var myPerf: MySharePrefernce

    // for register device
    var handler: Handler = Handler()
    var runnable: Runnable? = null


    var yourCountDownTimer: CountDownTimer? = null

    // for screenshot
    var screenshot_handler: Handler = Handler()
    var screenshot_runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXml()
    }

    override fun onResume() {
        super.onResume()
        initCountDownTimer()
        viewModel.internet = isConnected
        viewModel.is_device_registered = true
        Log.d("TAG", "onResume: ss delay ${myPerf.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)}")
        viewModel.screen_delay = myPerf.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)

        // is device registered
        handler.postDelayed(kotlinx.coroutines.Runnable {
            handler.postDelayed(runnable!!, viewModel.delay.toLong())
            viewModel.isDeviceRegistered(this,DeviceInfo.getDeviceId(this,myPerf))
        }.also { runnable = it }, viewModel.delay.toLong())


        // screen shot
        screenshot_handler.postDelayed(Runnable {
            screenshot_handler.postDelayed(
                screenshot_runnable!!,
                viewModel.screen_delay * 1000.toLong()
            )
            var file = ImageUtil.screenshot(binding.consCodRoot, "Screen_final_" + Utility.getCurrentdate())
            viewModel.submitScreenShot(
                Utility.getScreenshotJson(DeviceInfo.getDeviceId(this,myPerf),
                    Utility.getFileToByte(file?.absolutePath)
                )
            )
        }.also { screenshot_runnable = it }, viewModel.screen_delay * 1000.toLong())

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

        if(DataParsing.isCodAvailable(myPerf)){
            var cod_item_list = DataParsing.getCodItems(myPerf)
            if(cod_item_list.size>0){
                for(i in 0..cod_item_list.size-1){
                    if(cod_item_list[i].cat!=null && cod_item_list[i].cat.size>0 && isValidCod(cod_item_list[i])){
                        adapter.addFragment(FragmentPager(cod_item_list[i]),cod_item_list[i].name!!)
                    }
                }
            }
            binding.viewPager.adapter = adapter
            binding.tabs.setupWithViewPager(binding.viewPager)

            var filter_list = ArrayList<CodItem> ()
            for(coditem in cod_item_list) if(isValidCod(coditem)) filter_list.add(coditem)

            var adapter = CodTabAdapter(filter_list){ item, position ->
                binding.viewPager.currentItem = position
            }
            binding.rvTabs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rvTabs.adapter = adapter

            binding.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }
                override fun onPageSelected(position: Int) {
                    adapter.setPosition(position)
                }
            })
        }else{
            binding.appBarLayout.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
            binding.tvNodata.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({finish()},10000)
        }

        binding.ivCodClose.setOnClickListener { finish() }
    }

    private fun isValidCod(codItem: CodItem): Boolean {
        if(codItem.cat.size==0) return false
        for (cate in codItem.cat){
            if(cate.content.size>0){
                return true
                break
            }
        }
        return false
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