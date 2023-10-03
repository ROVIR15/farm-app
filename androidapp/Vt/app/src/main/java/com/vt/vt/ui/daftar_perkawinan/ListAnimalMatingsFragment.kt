package com.vt.vt.ui.daftar_perkawinan

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem
import com.vt.vt.databinding.FragmentListAnimalMatingsBinding
import com.vt.vt.ui.daftar_perkawinan.bottom_sheet_dialog.AddListBreedingDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAnimalMatingsFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentListAnimalMatingsBinding? = null
    private val binding get() = _binding!!

    private val listAnimalMatingsViewModel: ListAnimalMatingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListAnimalMatingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarListAnimalMatings.topAppBar.apply {
                title = "Daftar Perkawinan"
                setNavigationOnClickListener { findNavController().popBackStack() }
                inflateMenu(R.menu.menu_goods_and_service)
                setOnMenuItemClickListener(this@ListAnimalMatingsFragment)
            }
        }
        observerView()
    }

    private fun observerView() {
        listAnimalMatingsViewModel.getAllBreedings()
        listAnimalMatingsViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        listAnimalMatingsViewModel.breedingEmitter.observe(viewLifecycleOwner) { data ->
            binding.dataEmpty.isEmpty.isVisible = data.isEmpty()
            setupRecyclerView(data)
        }
        listAnimalMatingsViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView(data: List<BreedingResponseItem?>) {
        val adapter = ListAnimalMatingsAdapter()
        adapter.submitList(data)
        with(binding) {
            recyclerViewListAnimalMatings.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerViewListAnimalMatings.adapter = adapter
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_goods_and_services -> {
                val addListBreedingDialogFragment = AddListBreedingDialogFragment()
                addListBreedingDialogFragment.show(
                    childFragmentManager,
                    addListBreedingDialogFragment::class.java.simpleName
                )
                return true
            }
        }
        return false
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

}