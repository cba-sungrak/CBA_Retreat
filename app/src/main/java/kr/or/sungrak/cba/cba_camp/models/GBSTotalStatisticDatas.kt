package kr.or.sungrak.cba.cba_camp.models

data class GBSTotalStatisticDatas(
    val `data`: List<GBSTotalStatisticData>

)

data class GBSTotalStatisticData(
        val attended: Int,
        val date: String,
        val gbsId: Int,
        val gbsLevel: String,
        val registered: Int
        ) {
    val percent: Int
        get() = (attended.toDouble() / registered.toDouble() * 100.0).toInt()

//    fun onClick(view: View) {
//        Toast.makeText(view.context, "Clicked: $gbsId", Toast.LENGTH_SHORT).show()
//                        fragmentManager!!.beginTransaction().replace(R.id.fragment_container, GBSStepStatisticFragment(date!!, gbsId)).addToBackStack(null).commit()
//
//    }
}
