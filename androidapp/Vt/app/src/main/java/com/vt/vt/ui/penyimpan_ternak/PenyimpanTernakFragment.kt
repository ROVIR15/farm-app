package com.vt.vt.ui.penyimpan_ternak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.local.cobahilt.model.Animal
import com.vt.vt.databinding.FragmentPenyimpanTernakBinding
import com.vt.vt.ui.penyimpan_ternak.adapter.PenyimpananTernakAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PenyimpanTernakFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentPenyimpanTernakBinding? = null
    private val binding get() = _binding!!

    private val penyimpanTernakViewModel: PenyimpanTernakViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPenyimpanTernakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            this.appBarPenyimpananTernak.topAppBar.apply {
                title = "Penyimpanan Ternak"
                setNavigationIcon(R.drawable.baseline_arrow_back_24).apply {
                    setNavigationOnClickListener {
                        view.findNavController().popBackStack()
                    }
                }
                inflateMenu(R.menu.menu_penyimpan_ternak)
                setOnMenuItemClickListener(this@PenyimpanTernakFragment)
            }
        }
        penyimpanTernakViewModel.animalName.observe(viewLifecycleOwner) { animal ->
            setRecyclerView(animal)
        }
    }

    private fun setRecyclerView(data: List<Animal>) {
        val penyimpanTernakAdapter = PenyimpananTernakAdapter(data)
        with(binding) {
            rvPenyimpananTernak.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvPenyimpananTernak.adapter = penyimpanTernakAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_area -> {
                view?.findNavController()
                    ?.navigate(R.id.action_penyimpanTernakFragment_to_dataAreaFragment)
                return true
            }
        }
        return false
    }

}