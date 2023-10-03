package com.vt.vt.ui.detail_area_block

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.vt.vt.R
import com.vt.vt.databinding.FragmentDetailAreaBlockBinding
import com.vt.vt.ui.detail_area_block.adapter.ViewPagerDetailAreaBlockAdapter
import com.vt.vt.ui.detail_area_block.bottom_sheet_dialog.AddAreaBlockDialogFragment
import com.vt.vt.ui.edit_area_block.AreaBlockViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAreaBlockFragment : Fragment(), Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private var _binding: FragmentDetailAreaBlockBinding? = null
    private val binding get() = _binding!!

    private val dataAreaViewModel by viewModels<DataAreaViewModel>()

    private var receiveId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAreaBlockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiveId = arguments?.getInt("id").toString()
        dataAreaViewModel.getBlockArea(receiveId)

        with(binding) {
            toolbarDetailArea.apply {
                title = "Detail Area/Block Name"
                setNavigationOnClickListener { findNavController().popBackStack() }
                setOnMenuItemClickListener(this@DetailAreaBlockFragment)
            }
            btnEditAreaBlock.setOnClickListener(this@DetailAreaBlockFragment)
        }
        observeView()
        setupViewPager()
    }

    private fun observeView() {
        dataAreaViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getBlockArea.observe(viewLifecycleOwner) { blockArea ->
                with(binding) {
                    tvBlockName.text = blockArea?.name.toString()
                    tvDescBlockArea.text = blockArea?.description.toString()
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewPager() {
        val fragmentBundles = arrayListOf(
            Bundle().apply { putInt("areaBlockId", receiveId.toInt()) },
            Bundle().apply { putInt("areaBlockId", receiveId.toInt()) },
            Bundle().apply { putInt("areaBlockId", receiveId.toInt()) },
        )

        val adapter = ViewPagerDetailAreaBlockAdapter(
            fragmentBundles, requireActivity().supportFragmentManager, lifecycle
        )
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Daftar Kandang"
                1 -> tab.text = "Livestock"
                2 -> tab.text = "Pakan"
            }
        }.attach()
    }


    private fun showLoading(state: Boolean) {
//        binding.refresh.isRefreshing = state
        with(binding) {
            if (state) {
                loading.progressBar.visibility = View.VISIBLE
            } else {
                loading.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_cage_detail_area -> {
                val addAreaBlockDialog = AddAreaBlockDialogFragment()
                addAreaBlockDialog.show(
                    childFragmentManager,
                    addAreaBlockDialog::class.java.simpleName
                )
                return true
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_area_block -> {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
//                v.findNavController()
//                    .navigate(R.id.action_detailAreaBlockFragment_to_editAreaBlockFragment)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}