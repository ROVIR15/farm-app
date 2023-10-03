package com.vt.vt.ui.detail_area_block.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_kandang.AreaListKandangFragment
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_livestock.AreaListLivestockFragment
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan.AreaListPakanFragment

class ViewPagerDetailAreaBlockAdapter(
    private val fragmentBundle: ArrayList<Bundle>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentBundle.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragmentBundle[position].getInt("Id").let {
            when (position) {
                0 -> AreaListKandangFragment()
                1 -> AreaListLivestockFragment()
                2 -> AreaListPakanFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
        fragment.arguments = fragmentBundle[position]

        return fragment
    }
}