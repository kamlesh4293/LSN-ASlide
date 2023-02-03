package com.app.lsquared.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.lsquared.databinding.ActivitySplashBinding
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.utils.DataManager
import com.app.lsquared.utils.MySharePrefernce
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(){

    private lateinit var binding : ActivitySplashBinding

    @Inject
    lateinit var pref: MySharePrefernce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = MySharePrefernce(this)
        DataManager.createReportFile(pref)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}