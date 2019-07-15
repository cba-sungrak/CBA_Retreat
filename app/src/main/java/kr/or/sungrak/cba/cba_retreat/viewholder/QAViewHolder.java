package kr.or.sungrak.cba.cba_retreat.viewholder;

import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.Post;

public class QAViewHolder extends RecyclerView.ViewHolder {

    public TextView nameView;
    public TextView bodyView;
    public TextView timeView;
    public CardView cardView;
    public LinearLayout linearLayout;
    public LinearLayout nameLayout;

    public QAViewHolder(View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.qa_name);
        bodyView = itemView.findViewById(R.id.qa_body);
        timeView = itemView.findViewById(R.id.qa_time);
        cardView = itemView.findViewById(R.id.post_card_view);
        linearLayout = itemView.findViewById(R.id.qa_layout);
        nameLayout= itemView.findViewById(R.id.qa_name_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bindToPost(Post post) {
        nameView.setText(post.author);
        bodyView.setText(post.message);
        timeView.setText(post.time);
    }
}
