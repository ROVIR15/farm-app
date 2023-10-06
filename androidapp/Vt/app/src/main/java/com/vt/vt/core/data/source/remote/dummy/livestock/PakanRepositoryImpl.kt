package com.vt.vt.core.data.source.remote.dummy.livestock

class PakanRepositoryImpl : IPakan {
    override fun getPakan(): List<Pakan> {
        return listOf(
            Pakan(90, "22 Oktober 2023"),
            Pakan(82, "22 Oktober 2023"),
            Pakan(29, "22 Oktober 2023")
        )
    }
}