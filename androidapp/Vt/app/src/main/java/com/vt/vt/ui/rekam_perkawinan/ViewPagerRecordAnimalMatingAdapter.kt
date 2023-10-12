package com.vt.vt.ui.rekam_perkawinan

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vt.vt.ui.rekam_perkawinan.tab_layout.lambing_breeding.LambingsBreedingFragment
import com.vt.vt.ui.rekam_perkawinan.tab_layout.history_animal_mating.HistoryAnimalMatingFragment

class ViewPagerRecordAnimalMatingAdapter(fm: Fragment, private val bundle: Bundle) :
    FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = HistoryAnimalMatingFragment()
            1 -> fragment = LambingsBreedingFragment()
        }

        fragment?.arguments = bundle

        return fragment as Fragment
    }
}