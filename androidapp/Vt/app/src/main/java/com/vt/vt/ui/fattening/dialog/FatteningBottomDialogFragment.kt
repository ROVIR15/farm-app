package com.vt.vt.ui.fattening.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.model.LivestockOptionResponseItem
import com.vt.vt.databinding.FragmentFatteningBottomDialogBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FatteningBottomDialogFragment : BottomSheetDialogFragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentFatteningBottomDialogBinding? = null
    private val binding get() = _binding!!

    private val listLivestockAdapter by lazy { ListChooseLivestockAdapter() }
    private val livestockViewModel by viewModels<LivestockViewModel>()

    private val mBundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFatteningBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveNavigateTo = arguments?.getInt("navigate")
        livestockViewModel.getListOptionLivestock()
        with(binding) {
            listOptionLivestock.adapter = listLivestockAdapter
            listOptionLivestock.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.searchLivestock.setOnQueryTextListener(this@FatteningBottomDialogFragment)
            listLivestockAdapter.onClickListener = { livestockAdapter ->
                btnSave.setOnClickListener {
                    if (livestockAdapter.id != null) {
                        mBundle.putInt("livestockId", livestockAdapter.id)
                        if (receiveNavigateTo != null) {
                            findNavController().navigate(
                                receiveNavigateTo, mBundle
                            )
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Silahkan Pilih Ternak",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            btnCancel.setOnClickListener { dismiss() }
        }
        observerView()
    }

    private fun observerView() {
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.loading.isVisible = isLoading
        }
        livestockViewModel.livestockOptionEmitter.observe(viewLifecycleOwner) { livestock ->
            listLivestockOption(livestock)
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeFilteredLivestock(query: String) {
        livestockViewModel.filterLivestock(query).observe(viewLifecycleOwner) { filteredList ->
            listLivestockAdapter.submitList(filteredList)
        }
    }

    private fun listLivestockOption(data: List<LivestockOptionResponseItem>) {
        listLivestockAdapter.submitList(data)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            observeFilteredLivestock(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            observeFilteredLivestock(newText)
        }
        return true
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}