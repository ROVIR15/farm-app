package com.vt.vt.core.data.local.livestock

class LivestockRepositoryImpl: ILivestock {
    override fun getLivestock(): List<Livestock> {
        return listOf(
            Livestock("Livestock #001", "1 Tahun 5 Bulan"),
            Livestock("Livestock #002", "1 Tahun 5 Bulan"),
            Livestock("Livestock #003", "1 Tahun 5 Bulan")
        )
    }
}