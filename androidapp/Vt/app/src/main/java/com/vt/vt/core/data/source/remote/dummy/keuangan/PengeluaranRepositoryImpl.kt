package com.vt.vt.core.data.source.remote.dummy.keuangan

class PengeluaranRepositoryImpl : IPengeluaran {
    override fun getPengeluaran(): List<Pengeluaran> {
        val pengeluaran = mutableListOf<Pengeluaran>()
        val numPengeluaran = 10
        for (i in 1..numPengeluaran) {
            val _pengeluaran =
                Pengeluaran("Budget #${String.format("%03d", i)}", 20000)
            pengeluaran.add(_pengeluaran)
        }
        return pengeluaran
    }
}