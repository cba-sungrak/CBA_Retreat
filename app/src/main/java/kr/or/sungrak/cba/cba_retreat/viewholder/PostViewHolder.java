package kr.or.sungrak.cba.cba_retreat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView bodyView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.post_title);
        authorView = itemView.findViewById(R.id.post_author);
        bodyView = itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Post post) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        bodyView.setText(post.message);
    }
}
