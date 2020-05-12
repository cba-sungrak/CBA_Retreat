package kr.or.sungrak.cba.cba_camp.models

data class GBSStepStatisticDatas(
    val `data`: List<GBSStepStatisticData>
)

data class GBSStepStatisticData(
        val attended: Int,
        val checked: Boolean,
        val date: String,
        val leaderId: Any,
        val leaderName: String,
        val registered: Int
){
    val percent: Int
        get() = (attended.toDouble() / registered.toDouble() * 100.0).toInt()
}