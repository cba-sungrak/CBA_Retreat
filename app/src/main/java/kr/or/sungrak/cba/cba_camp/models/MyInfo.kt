package kr.or.sungrak.cba.cba_camp.models

import com.google.gson.annotations.SerializedName


data class MyInfo(
        @SerializedName("memId") val memId: String,
        @SerializedName("name") val name: String,
        @SerializedName("campus") val campus: String,
        @SerializedName("dt_birth") val dt_birth: String,
        @SerializedName("mobile") val mobile: String,
        @SerializedName("uid") val uid: String,
        @SerializedName("grade") val grade: String,
        @SerializedName("department") val department: String,
        @SerializedName("retreatGbsInfo") val retreatGbsInfo: MyRetreatGbsInfo,
        @SerializedName("gbsInfo") val gbsInfo: MyGbsInfo
)

data class MyGbsInfo(
        @SerializedName("gbs") val gbs: String,
        @SerializedName("position") val position: String
)

data class MyRetreatGbsInfo(
        @SerializedName("retreatId") val retreatId: Int,
        @SerializedName("gbs") val gbs: String,
        @SerializedName("position") val position: String
)