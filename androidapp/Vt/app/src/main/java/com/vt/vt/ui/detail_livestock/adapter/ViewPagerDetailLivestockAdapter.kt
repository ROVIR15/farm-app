package com.vt.vt.ui.detail_livestock.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vt.vt.ui.detail_livestock.tab_layout.bcs.BcsFragment
import com.vt.vt.ui.detail_livestock.tab_layout.beratbadan.WeightFragment
import com.vt.vt.ui.detail_livestock.tab_layout.kesehatan.HealthRecordsFragment
import com.vt.vt.ui.detail_livestock.tab_layout.pakan.PakanFragment

class ViewPagerDetailLivestockAdapter(
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
                0 -> WeightFragment()
                1 -> HealthRecordsFragment()
                2 -> BcsFragment()
                3 -> PakanFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
        fragment.arguments = fragmentBundle[position]

        return fragment
    }

}