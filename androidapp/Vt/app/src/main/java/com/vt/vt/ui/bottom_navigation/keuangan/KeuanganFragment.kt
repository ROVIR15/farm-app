package com.vt.vt.ui.bottom_navigation.keuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentKeuanganBinding

class KeuanganFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentKeuanganBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeuanganBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarKeuangan.topAppBar.apply {
                title = "Finance Summary"
                post {
                    inflateMenu(R.menu.menu_keuangan)
                    setOnMenuItemClickListener(this@KeuanganFragment)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_folder -> {
                Toast.makeText(requireContext(), "Press1", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}