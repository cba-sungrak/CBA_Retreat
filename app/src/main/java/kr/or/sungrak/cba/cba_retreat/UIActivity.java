package kr.or.sungrak.cba.cba_retreat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UIActivity extends Activity{

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_layout);
        imageView = findViewById(R.id.imageView2);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        // Create a reference with an initial file path and name
        Intent i = this.getIntent();
        String image = i.getStringExtra("image");
        StorageReference pathReference = storageReference.child(image);

        // ImageView in your Activity
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .into(imageView);
    }
}
