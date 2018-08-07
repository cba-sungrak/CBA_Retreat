package kr.or.sungrak.cba.cba_retreat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import kr.or.sungrak.cba.cba_retreat.R;

public class ImageViewFragment extends Fragment {
    String mImage;
    @SuppressLint("ValidFragment")
    public ImageViewFragment(String image) {
        mImage = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.ui_layout, container, false);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        // Create a reference with an initial file path and name
        StorageReference pathReference = storageReference.child(mImage);

        // ImageView in your Activity
        ImageView imageView = rootView.findViewById(R.id.imageView2);

// Load the image using Glide
//       // ImageView in your Activity
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .into(imageView);
        return rootView;
    }
    // Reference to an image file in Firebase Storage

}
