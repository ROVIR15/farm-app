package com.vt.vt.ui.barang_dan_jasa

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.products.dto.ProductResponseItem
import com.vt.vt.databinding.FragmentItemsAndServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsAndServiceFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentItemsAndServiceBinding? = null
    private val binding get() = _binding!!

    private val listItemsAndServiceViewModel by viewModels<ListItemsAndServiceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsAndServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarDataGoodsAndServices.topAppBar.apply {
                title = "Lihat Barang/Jasa"
                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                setNavigationOnClickListener { findNavController().popBackStack() }
                inflateMenu(R.menu.menu_goods_and_service)
                setOnMenuItemClickListener(this@ItemsAndServiceFragment)
            }
        }
        observerView()
    }

    private fun observerView() {
        listItemsAndServiceViewModel.apply {
            getAllProducts()
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            productsEmitter.observe(viewLifecycleOwner) { products ->
                setRecyclerView(products)
            }
            isDeletedProduct.observe(viewLifecycleOwner) {
                getAllProducts()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecyclerView(data: List<ProductResponseItem>) {
        val adapter = ListItemsAndServiceAdapter(listItemsAndServiceViewModel)
        adapter.submitList(data)
        with(binding) {
            listBarangJasa.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            listBarangJasa.adapter = adapter
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_goods_and_services -> {
                IS_UPDATE_DATA = false
                view?.findNavController()
                    ?.navigate(R.id.action_dataBarangDanJasaFragment_to_addDataBarangJasaFragment)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        var IS_UPDATE_DATA: Boolean = false
    }
}