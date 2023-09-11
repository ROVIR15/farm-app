package com.vt.vt.core.data.local.cobahilt.repository

import com.vt.vt.core.data.local.cobahilt.model.Animal
import com.vt.vt.core.data.local.cobahilt.model.IAnimal

class AnimalRepositoryImpl : IAnimal {
    override fun getName(): List<Animal> {
        return listOf(
            Animal("Ayam", 12),
            Animal("Sapi", 9)
        )
    }
}