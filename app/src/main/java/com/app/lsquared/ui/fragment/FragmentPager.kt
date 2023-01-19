package com.app.lsquared.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lsquared.databinding.FragmentCodPagerBinding
import com.app.lsquared.model.CodItem
import com.app.lsquared.ui.activity.CODActivity
import com.app.lsquared.ui.activity.CodContentActivity
import com.app.lsquared.ui.adapter.CodVerticalAdapter

class FragmentPager(var codItem: CodItem) : Fragment(){

    private lateinit var binding : FragmentCodPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCodPagerBinding.inflate(inflater,container,false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {

        if(codItem.cat!=null && codItem.cat.size>0){
            binding.rvCodVerical.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CodVerticalAdapter(codItem.cat) { item, position ->
                    (activity as CODActivity).stopCounter()
                    startActivity(Intent(requireContext(), CodContentActivity::class.java)
                        .putExtra(CodContentActivity.EXTRA_ITEM_DATA,item))
                }
            }
        }


//        var image_content = DataParsing.getFilterdContent(content,Constant.CONTENT_IMAGE)
//        var video_content = DataParsing.getFilterdContent(content,Constant.CONTENT_VIDEO)
//        var web_content = DataParsing.getFilterdContent(content,Constant.CONTENT_WEB)

        // images
//        if(image_content.size > 0)
//            binding.rvFragmentImages.apply {
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                adapter = CODHoriContentAdapter(image_content) { item, position ->
//                    startActivity(Intent(requireContext(),CodContentActivity::class.java)
//                        .putExtra(CodContentActivity.EXTRA_ITEM_DATA,item))
//                }
//            }
//        else{
//            binding.tvFragmentpagerImagTitle.visibility = View.GONE
//            binding.rvFragmentImages.visibility = View.GONE
//        }
//
//         video
//        if(image_content.size > 0)
//            binding.rvFragmentVideos.apply {
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                adapter = CODHoriContentAdapter(video_content) { item, position ->
//                    startActivity(Intent(requireContext(),CodContentActivity::class.java)
//                        .putExtra(CodContentActivity.EXTRA_ITEM_DATA,item))
//                }
//            }
//        else{
//            binding.tvFragmentpagerVideoTitle.visibility = View.GONE
//            binding.rvFragmentVideos.visibility = View.GONE
//        }


        // webview
//        if(image_content.size > 0)
//            binding.rvFragmentWeb.apply {
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                adapter = CODHoriContentAdapter(web_content) { item, position ->
//                    startActivity(Intent(requireContext(),CodContentActivity::class.java)
//                        .putExtra(CodContentActivity.EXTRA_ITEM_DATA,item))
//                }
//            }
//        else{
//            binding.tvFragmentpagerWebTitle.visibility = View.GONE
//            binding.rvFragmentWeb.visibility = View.GONE
//        }

    }

}

