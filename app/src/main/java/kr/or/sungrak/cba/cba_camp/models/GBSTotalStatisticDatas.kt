package kr.or.sungrak.cba.cba_camp.models

data class GBSTotalStatisticDatas(
    val `data`: List<GBSTotalStatisticData>

)

data class GBSTotalStatisticData(
        val attended: Int,
        val date: String,
        val gbsId: Any,
        val gbsLevel: String,
        val registered: Int
        ) {
    val percent: Int
        get() = (attended.toDouble() / registered.toDouble() * 100.0).toInt()
}

