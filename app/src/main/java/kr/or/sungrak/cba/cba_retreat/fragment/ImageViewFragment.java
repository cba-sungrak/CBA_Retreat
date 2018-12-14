package kr.or.sungrak.cba.cba_retreat.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import kr.or.sungrak.cba.cba_retreat.FCM.GlideApp;
import kr.or.sungrak.cba.cba_retreat.R;
import uk.co.senab.photoview.PhotoView;

@SuppressLint("ValidFragment")
public class ImageViewFragment extends Fragment {
    String mImage;

    StorageReference pathReference;
    PhotoView imageView;
    SharedPreferences sharedPref;

    @SuppressLint("ValidFragment")
    public ImageViewFragment(String image) {
        mImage = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.ui_layout, container, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        // Create a reference with an initial file path and name
        pathReference = storageReference.child(mImage);

        // ImageView in your Activity
        imageView = rootView.findViewById(R.id.imageView2);


        pathReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String previousUpdateTime = sharedPref.getString(mImage, "");
                String updatetime = String.valueOf(storageMetadata.getCreationTimeMillis());
                if (!previousUpdateTime.equalsIgnoreCase(updatetime)) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(mImage, updatetime);
                    editor.commit();
                    Log.d("CBA_ImageViewFragment", "image/" + mImage + " UpdateTime/" + updatetime);
                    GlideApp.with(getActivity())
                            .load(pathReference)
                            .signature(new ObjectKey(updatetime))
                            .into(imageView);
                }
            }
        });
// Load the image using Glide
//       // ImageView in your Activity
        String previousUpdateTime = sharedPref.getString(mImage, "");
        GlideApp.with(getActivity())
                .load(pathReference)
                .signature(new ObjectKey(previousUpdateTime))
                .into(imageView);
        return rootView;
    }
    // Reference to an image file in Firebase Storage

}
