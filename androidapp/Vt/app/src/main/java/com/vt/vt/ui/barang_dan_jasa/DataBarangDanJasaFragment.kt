package com.vt.vt.ui.barang_dan_jasa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentDataBarangDanJasaBinding

class DataBarangDanJasaFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentDataBarangDanJasaBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_goods_and_services -> {
                return true
            }
        }
        return false
    }
}