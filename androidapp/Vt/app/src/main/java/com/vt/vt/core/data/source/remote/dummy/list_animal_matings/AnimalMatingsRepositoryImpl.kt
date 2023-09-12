package com.vt.vt.core.data.source.remote.dummy.list_animal_matings

class AnimalMatingsRepositoryImpl : IAnimalMatings {
    override fun getAnimalMaatings(): List<AnimalMatings> {
        val animalMatings = mutableListOf<AnimalMatings>()
        val numCages = 10
        for (i in 1..numCages) {
            val animalMating =
                AnimalMatings("Pasangan #${String.format("%03d", i)}", "12 December 2023")
            animalMatings.add(animalMating)
        }
        return animalMatings
    }
}