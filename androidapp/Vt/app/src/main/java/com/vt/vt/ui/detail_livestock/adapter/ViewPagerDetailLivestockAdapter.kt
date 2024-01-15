package com.vt.vt.ui.detail_livestock.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vt.vt.ui.detail_livestock.tab_layout.bcs.BcsFragment
import com.vt.vt.ui.detail_livestock.tab_layout.beratbadan.WeightFragment
import com.vt.vt.ui.detail_livestock.tab_layout.kesehatan.HealthRecordsFragment
import com.vt.vt.ui.detail_livestock.tab_layout.milk_production.MilkProductionFragment
import com.vt.vt.ui.detail_livestock.tab_layout.pakan.PakanFragment
import com.vt.vt.ui.detail_livestock.tab_layout.tinggi_badan.HeightTabLayoutFragment

class ViewPagerDetailLivestockAdapter(
    fm: Fragment,
    private val mBundle: Bundle
) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = WeightFragment()
            1 -> fragment = HealthRecordsFragment()
            2 -> fragment = BcsFragment()
            3 -> fragment = PakanFragment()
            4 -> fragment = HeightTabLayoutFragment()
            5 -> fragment = MilkProductionFragment()
        }
        fragment?.arguments = mBundle
        return fragment as Fragment
    }

}