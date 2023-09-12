package com.vt.vt.core.data.source.remote.dummy.list_animal_cage

class AnimalCageRepositoryImpl : IAnimalCage {
    override fun getListAnimalCage(): List<AnimalCage> {
        val animalCages = mutableListOf<AnimalCage>()
        val numCages = 10
        for (i in 1..numCages) {
            val animalCage =
                AnimalCage("Sled #${String.format("%03d", i)}", "1 Livestock was live here")
            animalCages.add(animalCage)
        }
        return animalCages
    }
}