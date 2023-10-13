package com.vt.vt.ui.rekam_kesehatan

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentRekamKesehatanBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import com.vt.vt.utils.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RekamKesehatanFragment : Fragment() {

    private var _binding: FragmentRekamKesehatanBinding? = null
    private val binding get() = _binding!!

    private val rekamKesehatanViewModel by viewModels<RekamKesehatanViewModel>()
    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRekamKesehatanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())
        with(binding) {
            this.appBarLayout.topAppBar.apply {
                title = "Rekam Kesehatan"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            btnSimpanRekamKesehatan.setOnClickListener {
                val description = edtDescriptionRekamKesehatan.text.toString()
                val date = getCurrentDate()
                if (description.isNotEmpty() && date.isNotEmpty()) {
                    rekamKesehatanViewModel.createBcsRecordById(receiveId, date, description)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Silahkan Lengkapi Kolom Deskripsi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btnBatalRekamKesehatan.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        observewView()
    }

    @SuppressLint("SetTextI18n")
    private fun observewView() {
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getLivestockById.observe(viewLifecycleOwner) { livestock ->
                with(binding) {
                    tvTitleLivestock.text = livestock?.name.toString()
                    tvInfo.text = livestock?.info.toString()
                    tvDescriptionLivestock.text = livestock?.description.toString()
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        rekamKesehatanViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { showLoading(it) }
            createHealthRecordEmitter.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Toast.makeText(
                    requireActivity(),
                    "${it.status} menambahkan kesehatan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSimpanRekamKesehatan.isEnabled = !state
            btnBatalRekamKesehatan.isEnabled = !state

            btnSimpanRekamKesehatan.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )
            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

}