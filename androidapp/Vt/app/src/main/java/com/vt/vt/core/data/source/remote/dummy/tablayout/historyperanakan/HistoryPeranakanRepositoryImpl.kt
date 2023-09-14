package com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan

class HistoryPeranakanRepositoryImpl : IHistoryPeranakan {
    override fun getHistoryPeranakan(): List<HistoryPeranakan> {
        val listHistoryPeranakan = mutableListOf<HistoryPeranakan>()
        val numItemHistory = 10
        for (i in 1..numItemHistory) {
            val historyPeranakan =
                HistoryPeranakan(
                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book #${
                        String.format(
                            "%03d",
                            i
                        )
                    }"
                )
            listHistoryPeranakan.add(historyPeranakan)
        }
        return listHistoryPeranakan
    }
}