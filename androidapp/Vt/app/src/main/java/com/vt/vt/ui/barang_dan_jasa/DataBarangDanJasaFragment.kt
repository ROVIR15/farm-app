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
import com.vt.vt.core.data.source.remote.products.model.ProductResponseItem
import com.vt.vt.databinding.FragmentDataBarangDanJasaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataBarangDanJasaFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentDataBarangDanJasaBinding? = null
    private val binding get() = _binding!!

    private val listBarangDanJasaViewModel by viewModels<ListBarangDanJasaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBarangDanJasaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarDataGoodsAndServices.topAppBar.apply {
                title = "View Barang/Jasa"
                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                setNavigationOnClickListener { findNavController().popBackStack() }
                inflateMenu(R.menu.menu_goods_and_service)
                setOnMenuItemClickListener(this@DataBarangDanJasaFragment)
            }
        }
        observerView()
    }

    private fun observerView() {
        listBarangDanJasaViewModel.apply {
            getAllProducts()
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            productsEmitter.observe(viewLifecycleOwner) { products ->
                setRecyclerView(products)
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecyclerView(data: List<ProductResponseItem>) {
        val adapter = ListBarangDanJasaAdapter()
        adapter.submitList(data)
        with(binding) {
            listBarangJasa.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            listBarangJasa.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
//            btnSaveEditLivestock.isEnabled = !state
//            btnCancelEditLivestock.isEnabled = !state
//
//            btnSaveEditLivestock.setBackgroundColor(
//                if (state) Color.GRAY
//                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
//            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    companion object {
        var IS_UPDATE_DATA: Boolean = false
    }
}