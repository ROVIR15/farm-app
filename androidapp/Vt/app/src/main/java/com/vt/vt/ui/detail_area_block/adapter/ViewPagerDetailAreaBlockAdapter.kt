package com.vt.vt.ui.detail_area_block.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_kandang.ListEnclosureAreasFragment
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_livestock.ListLivestockAreasFragment
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan.ListFoodAreasFragment

class ViewPagerDetailAreaBlockAdapter(
    private val fragmentBundle: Bundle,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ListEnclosureAreasFragment()
            1 -> fragment = ListLivestockAreasFragment()
            2 -> fragment = ListFoodAreasFragment()
        }

        fragment?.arguments = fragmentBundle

        return fragment as Fragment
    }
}