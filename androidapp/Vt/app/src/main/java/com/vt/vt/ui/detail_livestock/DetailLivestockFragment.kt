package com.vt.vt.ui.detail_livestock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.vt.vt.R
import com.vt.vt.databinding.FragmentDetailLivestockBinding
import com.vt.vt.ui.detail_livestock.adapter.ViewPagerDetailLivestockAdapter
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailLivestockFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentDetailLivestockBinding? = null
    private val binding get() = _binding!!

    private val editLivestockViewModel by viewModels<EditLivestockViewModel>()
    private var receiveId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailLivestockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiveId = arguments?.getInt("id").toString()
        editLivestockViewModel.getLivestockById(receiveId)

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
        observerView()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        editLivestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                binding.loading.progressBar.isVisible = it
            }
            getLivestockById.observe(viewLifecycleOwner) { data ->
                if (data != null) {
                    val dynamicPattern = Regex("S-\\d+")
                    val newInfo = if (dynamicPattern.containsMatchIn(data.info)) {
                        data.info.replace(dynamicPattern, "")
                    } else {
                        data.info
                    }
                    binding.tvTitleLivestock.text = newInfo.trim()
                    binding.tvInfo.text = data.info.trim()
                    binding.tvDescriptionLivestock.text = data.description.trim()
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewPager() {
        val fragmentBundles = Bundle().apply { putInt("livestockId", receiveId.toInt()) }
        val adapter = ViewPagerDetailLivestockAdapter(this, fragmentBundles)
        with(binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_action_edit_profile_livestock -> {
                val mBundle = Bundle()
                mBundle.putInt("id", receiveId.toInt())
                this.findNavController()
                    .navigate(R.id.action_detailLivestockFragment_to_editLivestockFragment, mBundle)
                return true
            }
        }
        return false
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1_breeding,
            R.string.tab_text_2_breeding,
            R.string.tab_text_3_breeding,
            R.string.tab_text_4_breeding,
            R.string.tab_text_5_breeding,
        )
    }
}