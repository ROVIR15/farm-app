package com.vt.vt.ui.pemberian_ternak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentPemberianTernakBinding

class PemberianTernakFragment : Fragment() {

    private var _binding: FragmentPemberianTernakBinding? = null
    private val binding get() = _binding!!

    private lateinit var mBundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPemberianTernakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("id")

        mBundle = Bundle().apply {
            receiveBlockId?.let { putInt("blockId", it) }
        }

        with(binding) {
            this.appBarLayout.topAppBar.apply {
                title = "Pemberian Ternak"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_hijauanFragment, mBundle)
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView2.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_kimiaFragment, mBundle)
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView3.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_vitaminFragment, mBundle)
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView4.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_tambahanFragment, mBundle)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        val EXTRA_CARD_NUMBER_ONE = "EXTRA_CARD_NUMBER_ONE"
    }
}