package kr.or.sungrak.cba.cba_retreat.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.common.Tag;
import kr.or.sungrak.cba.cba_retreat.dialog.PostDialog;
import kr.or.sungrak.cba.cba_retreat.models.Post;
import kr.or.sungrak.cba.cba_retreat.viewholder.QAViewHolder;

public class QAListFragment extends Fragment {

    private static final String TAG = "QAListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Post, QAViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    FloatingActionButton mFab;

    public QAListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.qa_all_posts, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        mFab = rootView.findViewById(R.id.fab);
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    mFab.show();
                } else if (dy > 0) {
                    mFab.hide();
                }
            }
        });

        mFab.setOnClickListener(view -> showPostDialog());

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Post, QAViewHolder>(options) {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onBindViewHolder(@NonNull QAViewHolder qaViewHolder, int i, @NonNull Post post) {
//                if (model.isStaff.equalsIgnoreCase("봉사자") || (model.isStaff.equalsIgnoreCase("공지"))) {
//                    viewHolder.authorImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.app_icon, getActivity().getTheme()));
//                    int colorRed = getActivity().getResources().getColor(R.color.grey_200);
//                    viewHolder.cardView.setCardBackgroundColor(colorRed);
//                }
                qaViewHolder.bindToPost(post, getContext());
            }

            @Override
            public QAViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new QAViewHolder(inflater.inflate(R.layout.qa_item, viewGroup, false));
            }

        };
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child(Tag.RETREAT_CBA).child(Tag.MESSAGE).limitToFirst(1000);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    private void showPostDialog() {
        final PostDialog postDialog = new PostDialog(getActivity());

        postDialog.show();
        postDialog.setOnDismissListener(dialog -> {
        });

    }
}