package com.vt.vt.core.data.source.remote.dummy.cobahilt.repository

import com.vt.vt.core.data.source.remote.dummy.cobahilt.model.Animal
import com.vt.vt.core.data.source.remote.dummy.cobahilt.model.IAnimal

class AnimalRepositoryImpl : IAnimal {
    override fun getName(): List<Animal> {
        return listOf(
            Animal("Ayam", 12),
            Animal("Sapi", 9)
        )
    }
}