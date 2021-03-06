package kr.or.sungrak.cba.cba_camp.viewholder;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView;
    public ImageView authorImageView;
    public TextView bodyView;
    public TextView timeView;
    public CardView cardView;

    public PostViewHolder(View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.post_name);
        authorImageView = itemView.findViewById(R.id.post_author_photo);
        bodyView = itemView.findViewById(R.id.post_body);
        timeView = itemView.findViewById(R.id.post_time);
        cardView = itemView.findViewById(R.id.post_card_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bindToPost(Post post, Context context) {
        nameView.setText(post.author);
        bodyView.setText(post.message);
        timeView.setText(post.time);
    }
}
