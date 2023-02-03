package com.app.lsquared.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.lsquared.R
import com.google.android.youtube.player.YouTubePlayer

class FragmentYouTube : Fragment() , YouTubePlayer.Provider{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = layoutInflater.inflate(R.layout.view_youtube,container,false)
        return view
    }

    override fun initialize(p0: String?, p1: YouTubePlayer.OnInitializedListener?) {
    }
}