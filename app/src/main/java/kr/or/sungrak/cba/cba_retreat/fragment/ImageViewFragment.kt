package kr.or.sungrak.cba.cba_retreat.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kr.or.sungrak.cba.cba_retreat.FCM.GlideApp
import kr.or.sungrak.cba.cba_retreat.R
import kr.or.sungrak.cba.cba_retreat.common.Tag
import uk.co.senab.photoview.PhotoView

@SuppressLint("ValidFragment")
class ImageViewFragment @SuppressLint("ValidFragment")
constructor(private var mImage: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.ui_layout, container, false)

        val storageReference = FirebaseStorage.getInstance().reference
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val fdb = FirebaseDatabase.getInstance().reference
        val dbReference = fdb.child(Tag.CBA_DB)
        val imageView: PhotoView
        // ImageView in your Activity
        imageView = rootView.findViewById(R.id.singleImageView)

        dbReference.child("images").child(mImage).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Toast.makeText(activity, "" + dataSnapshot.value, Toast.LENGTH_SHORT).show()
                        mImage = dataSnapshot.value as String
                        val pathReference = storageReference.child(mImage)
                        pathReference.metadata.addOnSuccessListener { storageMetadata ->
                            val previousUpdateTime = sharedPref.getString(mImage, "")
                            val updatetime = storageMetadata.creationTimeMillis.toString()
                            if (!previousUpdateTime!!.equals(updatetime, ignoreCase = true)) {
                                val editor = sharedPref.edit()
                                editor.putString(mImage, updatetime)
                                editor.commit()
                                Log.d("CBA_ImageViewFragment", "image/$mImage UpdateTime/$updatetime")
                                GlideApp.with(activity!!)
                                        .load(pathReference)
                                        .signature(ObjectKey(updatetime))
                                        .into(imageView)
                            }
                        }

                        val previousUpdateTime = sharedPref.getString(mImage, "")
                        GlideApp.with(activity!!)
                                .load(pathReference)
                                .signature(ObjectKey(previousUpdateTime!!))
                                .into(imageView)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }

                })

        return rootView
    }
    // Reference to an image file in Firebase Storage

}