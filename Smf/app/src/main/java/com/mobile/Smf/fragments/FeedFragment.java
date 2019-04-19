package com.mobile.Smf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.Smf.model.Post;
import com.mobile.Smf.util.PostRecyclerViewAdapter;
import com.mobile.Smf.R;
import com.mobile.Smf.model.Feed;

import java.util.ArrayList;

import static com.mobile.Smf.model.Feed.getFeedSingleton;

public class FeedFragment extends Fragment {

    private Feed feed;
    private RecyclerView feedRecyclerView;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View feedView = inflater.inflate(R.layout.fragment_feed, container, false);

        feed = getFeedSingleton();

        feedRecyclerView = (RecyclerView) feedView.findViewById(R.id.feed_recyclerview);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        startRecyclerView();

        return feedView;
    }

    private void startRecyclerView() {
        postRecyclerViewAdapter = new PostRecyclerViewAdapter(feed.getFeedAsList());
        feedRecyclerView.setAdapter(postRecyclerViewAdapter);
    }

    public void updateRecyclerView() {
        startRecyclerView();
        // todo implement proper updating...
    }

}
