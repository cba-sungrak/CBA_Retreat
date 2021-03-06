package kr.or.sungrak.cba.cba_camp.fragment

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
import kr.or.sungrak.cba.cba_camp.FCM.GlideApp
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.common.Tag
import kr.or.sungrak.cba.cba_camp.dialog.MyProgessDialog

@SuppressLint("ValidFragment")
class ImageViewFragment @SuppressLint("ValidFragment")
constructor(private var mImage: String) : Fragment() {
    private lateinit var myDialog: MyProgessDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.ui_layout, container, false)
        lateinit var storageReference: StorageReference

        when (CBAUtil.getRetreat(activity)) {
            Tag.RETREAT_CBA -> {
                storageReference = FirebaseStorage.getInstance().reference.child(Tag.RETREAT_CBA)
                rootView.imageViewLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.cba_bg)
            }
            Tag.RETREAT_SUNGRAK -> {
                storageReference = FirebaseStorage.getInstance().reference.child(Tag.RETREAT_SUNGRAK)
                rootView.imageViewLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.sub_bg)
            }
        }

        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
//        val imageView: PhotoView
        val pathReference = storageReference.child(mImage)
        // ImageView in your Activity
        val imageView = rootView.singleImageView

        val previousUpdateTime = sharedPref.getString(mImage, "")
        myDialog = MyProgessDialog(requireContext())
        if (previousUpdateTime != null) {
            if (previousUpdateTime.isEmpty()) {
                myDialog.showProgressDialog()
            }
        }


        pathReference.metadata.addOnSuccessListener { storageMetadata ->
            val updateTime = storageMetadata.creationTimeMillis.toString()
            if (!previousUpdateTime!!.equals(updateTime, ignoreCase = true)) {
                val editor = sharedPref.edit()
                editor.putString(mImage, updateTime)
                editor.commit()
                Log.d("CBA_ImageViewFragment", "image/$mImage UpdateTime/$updateTime")
                GlideApp.with(requireActivity())
                        .load(pathReference)
                        .signature(ObjectKey(updateTime))
                        .into(imageView)
                myDialog.hideProgressDialog()
            }
        }

        GlideApp.with(requireActivity())
                .load(pathReference)
                .signature(ObjectKey(previousUpdateTime!!))
                .into(imageView)
//        myDialog.hideProgressDialog()
        return rootView
    }
}
