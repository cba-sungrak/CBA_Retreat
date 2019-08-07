package kr.or.sungrak.cba.cba_camp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MyInfo(
        @SerializedName("memId")
        @Expose
        val memId: Int,
        @SerializedName("retreat_id")
        @Expose
        val retreatId: Int,
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("campus")
        @Expose
        val campus: String,
        @SerializedName("retreatGbs")
        @Expose
        val retreatGbs: String,
        @SerializedName("originalGbs")
        @Expose
        val originalGbs: String,
        @SerializedName("position")
        @Expose
        val position: String,
        @SerializedName("dt_birth")
        @Expose
        val dtBirth: String,
        @SerializedName("sex")
        @Expose
        val sex: String,
        @SerializedName("mobile")
        @Expose
        val mobile: String,
        @SerializedName("uid")
        @Expose
        val uid: String,
        @SerializedName("grade")
        @Expose
        val grade: String
)