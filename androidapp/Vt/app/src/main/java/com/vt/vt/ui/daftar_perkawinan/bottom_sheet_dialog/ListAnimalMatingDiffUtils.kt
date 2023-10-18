package com.vt.vt.ui.daftar_perkawinan.bottom_sheet_dialog

import androidx.recyclerview.widget.DiffUtil
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem

class ListAnimalMatingDiffUtils(
    private val oldList: List<BreedingResponseItem>,
    private val newList: List<BreedingResponseItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}