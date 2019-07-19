package kr.or.sungrak.cba.cba_retreat.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
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
import kr.or.sungrak.cba.cba_retreat.common.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.common.Tag;
import kr.or.sungrak.cba.cba_retreat.dialog.QAPostDialog;
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
    String mNumber = "";
    Query mPostsQuery;

    public QAListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.qa_all_posts, container, false);


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

        switch (CBAUtil.getRetreat(getActivity())) {
            case Tag.RETREAT_CBA:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_CBA);
                mFab.setOnClickListener(view -> showPostDialog());
                mPostsQuery = getQuery(mDatabase);
                break;
            case Tag.RETREAT_SUNGRAK:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK);
                mFab.setOnClickListener(view -> showCheckPostDialog(CBAUtil.getPhoneNumber(getActivity()), CBAUtil.getPhoneNumber(getActivity())));
                mNumber = CBAUtil.getPhoneNumber(getActivity());
                mPostsQuery = getSRQuery(mDatabase);
                break;
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


        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(mPostsQuery, Post.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Post, QAViewHolder>(options) {

            @Override
            public QAViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new QAViewHolder(inflater.inflate(R.layout.qa_item, viewGroup, false));
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onBindViewHolder(@NonNull QAViewHolder qaViewHolder, int i, @NonNull Post post) {
                if (post.isStaff.equalsIgnoreCase("봉사자") || (post.isStaff.equalsIgnoreCase("공지"))) {
                    int colorRed = getActivity().getResources().getColor(R.color.grey_200);
                    qaViewHolder.cardView.setCardBackgroundColor(colorRed);
                    float density = getActivity().getResources().getDisplayMetrics().density;
                    int left = (int) (80 * density);
                    int right = (int) (18 * density);
                    qaViewHolder.linearLayout.setPadding(left, 0, right, 0);
                    qaViewHolder.nameLayout.setGravity(Gravity.RIGHT);
                    qaViewHolder.nameLayout.setPadding(0, 0, right, 0);
                }
                if (CBAUtil.isAdmin(getActivity())) {
                    qaViewHolder.itemView.setOnLongClickListener(v -> {
                        showCheckPostDialog(post.uid, post.author);
                        return false;
                    });
                }
                qaViewHolder.bindToPost(post);
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
        Query recentPostsQuery = databaseReference.child(Tag.MESSAGE).limitToFirst(1000);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    public Query getSRQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery;
        if (CBAUtil.isAdmin(getActivity())) {
            recentPostsQuery = databaseReference.child(Tag.MESSAGE).limitToFirst(1000);
        } else {
            recentPostsQuery = databaseReference.child(Tag.MESSAGE).orderByChild("uid").equalTo(CBAUtil.getPhoneNumber(getActivity())).limitToFirst(1000);
        }
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    public void showPostDialog() {
        final QAPostDialog postDialog = new QAPostDialog(getActivity(), "", "");

        postDialog.show();
        postDialog.setOnDismissListener(dialog -> {
            mRecycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        });

    }

    private void showCheckPostDialog(String uid, String auth) {
        final QAPostDialog postDialog = new QAPostDialog(getActivity(), uid, auth);

        postDialog.show();
        postDialog.setOnDismissListener(dialog -> {
            if (mAdapter.getItemCount() > 0) {
                mRecycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

    }
}