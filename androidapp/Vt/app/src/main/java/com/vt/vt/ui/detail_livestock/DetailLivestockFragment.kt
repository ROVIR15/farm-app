package com.vt.vt.ui.detail_livestock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.vt.vt.R
import com.vt.vt.databinding.FragmentDetailLivestockBinding
import com.vt.vt.ui.detail_livestock.adapter.ViewPagerDetailLivestockAdapter
import com.vt.vt.ui.detail_livestock.tab_layout.bcs.BcsFragment
import com.vt.vt.ui.detail_livestock.tab_layout.beratbadan.BeratBadanFragment
import com.vt.vt.ui.detail_livestock.tab_layout.kesehatan.KesehatanFragment
import com.vt.vt.ui.detail_livestock.tab_layout.pakan.PakanFragment

class DetailLivestockFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentDetailLivestockBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailLivestockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar2.apply {
                title = "Profile Livestock"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
                setOnMenuItemClickListener(this@DetailLivestockFragment)
            }
        }
        setupViewPager()
    }

    private fun setupViewPager() {
        val fragmentList = arrayListOf(
            BeratBadanFragment(), KesehatanFragment(), BcsFragment(), PakanFragment()
        )
        val adapter = ViewPagerDetailLivestockAdapter(
            fragmentList, requireActivity().supportFragmentManager, lifecycle
        )
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Berat Badan"
                1 -> tab.text = "Kesehatan"
                2 -> tab.text = "BCS"
                3 -> tab.text = "Pakan"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_action_edit_profile_livestock -> {
                this.findNavController()
                    .navigate(R.id.action_detailLivestockFragment_to_editLivestockFragment)
                return true
            }
        }
        return false
    }


}