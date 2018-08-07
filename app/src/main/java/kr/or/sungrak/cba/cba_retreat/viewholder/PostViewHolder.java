package kr.or.sungrak.cba.cba_retreat.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView authorImageView;
    public TextView bodyView;
    public TextView timeView;

    public PostViewHolder(View itemView) {
        super(itemView);

//        titleView = itemView.findViewById(R.id.post_title);
        authorView = itemView.findViewById(R.id.post_author);
        authorImageView = itemView.findViewById(R.id.post_author_photo);
        bodyView = itemView.findViewById(R.id.post_body);
        timeView = itemView.findViewById(R.id.post_time);
    }

    public void bindToPost(Post post, Context context) {
//        titleView.setText(post.title);
        authorView.setText(post.author);
        bodyView.setText(post.message);
        timeView.setText(post.time);
        if(post.author!=null&&post.author.equalsIgnoreCase("staff")){
            authorImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cba_icon,context.getTheme()));
        }
    }
}
