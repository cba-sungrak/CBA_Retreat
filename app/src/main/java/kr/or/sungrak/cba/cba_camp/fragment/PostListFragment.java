package kr.or.sungrak.cba.cba_camp.fragment;

import android.app.NotificationManager;
import android.content.Context;
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

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.common.CBAUtil;
import kr.or.sungrak.cba.cba_camp.common.Tag;
import kr.or.sungrak.cba.cba_camp.dialog.PostDialog;
import kr.or.sungrak.cba.cba_camp.models.MyInfo;
import kr.or.sungrak.cba.cba_camp.models.Post;
import kr.or.sungrak.cba.cba_camp.viewholder.PostViewHolder;

public class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    FloatingActionButton mFab;

    public PostListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);
        clearNoti();

        switch (CBAUtil.getRetreat(getActivity())) {
            case Tag.RETREAT_CBA:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_CBA);
                break;
            case Tag.RETREAT_SUNGRAK:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK);
                break;
        }

        mRecycler = rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        mFab = rootView.findViewById(R.id.fab);

        MyInfo memberInfo = CBAUtil.loadMyInfo(getContext());

        if ((memberInfo != null && (memberInfo.getRetreatGbs().equals("STAFF"))) || CBAUtil.isAdmin(getActivity())) {
            mFab.show();
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
        } else {
            mFab.hide();
        }

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

        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onBindViewHolder(PostViewHolder viewHolder, int position, final Post model) {
                switch (CBAUtil.getRetreat(getActivity())) {
                    case Tag.RETREAT_CBA:
                        viewHolder.bodyView.setTextSize(16);
                        break;
                    case Tag.RETREAT_SUNGRAK:
                        break;
                }

                viewHolder.bindToPost(model, getContext());
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
        Query recentPostsQuery = databaseReference.child(Tag.NOTI).limitToFirst(1000);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    private void showPostDialog() {
        final PostDialog postDialog = new PostDialog(getActivity());

        postDialog.show();
        postDialog.setOnDismissListener(dialog -> {
            mRecycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        });

    }

    private void clearNoti() {
        NotificationManager notiManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notiManager.cancelAll();
    }

}