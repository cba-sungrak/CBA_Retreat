package kr.or.sungrak.cba.cba_retreat.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.ui_layout.view.*
import kr.or.sungrak.cba.cba_retreat.FCM.GlideApp
import kr.or.sungrak.cba.cba_retreat.R
import kr.or.sungrak.cba.cba_retreat.common.CBAUtil
import kr.or.sungrak.cba.cba_retreat.common.Tag

@SuppressLint("ValidFragment")
class ImageViewFragment @SuppressLint("ValidFragment")
constructor(private var mImage: String) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.ui_layout, container, false)
        lateinit var storageReference: StorageReference

        when (CBAUtil.getRetreat(activity)) {
            Tag.RETREAT_CBA -> {
                storageReference = FirebaseStorage.getInstance().reference.child(Tag.RETREAT_CBA)
            }
            Tag.RETREAT_SUNGRAK -> {
                storageReference = FirebaseStorage.getInstance().reference.child(Tag.RETREAT_SUNGRAK)
                rootView.imageViewLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.sub_bg)
            }
        }

        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
//        val imageView: PhotoView

        // ImageView in your Activity
        val imageView = rootView.singleImageView

        val pathReference = storageReference.child(mImage)
        pathReference.metadata.addOnSuccessListener { storageMetadata ->
            val previousUpdateTime = sharedPref.getString(mImage, "")
            val updateTime = storageMetadata.creationTimeMillis.toString()
            if (!previousUpdateTime!!.equals(updateTime, ignoreCase = true)) {
                val editor = sharedPref.edit()
                editor.putString(mImage, updateTime)
                editor.commit()
                Log.d("CBA_ImageViewFragment", "image/$mImage UpdateTime/$updateTime")
                GlideApp.with(activity!!)
                        .load(pathReference)
                        .signature(ObjectKey(updateTime))
                        .into(imageView)
            }
        }

        val previousUpdateTime = sharedPref.getString(mImage, "")
        GlideApp.with(activity!!)
                .load(pathReference)
                .signature(ObjectKey(previousUpdateTime!!))
                .into(imageView)


        return rootView
    }
}
