package com.mobile.Smf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobile.Smf.model.Post;
import com.mobile.Smf.util.PostRecyclerViewAdapter;
import com.mobile.Smf.R;
import com.mobile.Smf.model.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.mobile.Smf.model.Feed.getFeedSingleton;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_IDLE;

public class FeedFragment extends Fragment implements Observer {

    private boolean debug = true;

    private Feed feed;
    private RecyclerView feedRecyclerView;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;

    private int scrollPosition;
    private int maxScrollPosition;
    private boolean hasIncreasedMaxScrollPosition = false;
    private boolean hasGottenOlderPosts = true;
    private boolean hasGottenNewerPosts = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View feedView = inflater.inflate(R.layout.fragment_feed, container, false);

        feed = getFeedSingleton(getContext());

        feedRecyclerView = (RecyclerView) feedView.findViewById(R.id.feed_recyclerview);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        startRecyclerView();

        return feedView;
    }

    private void startRecyclerView() {
        postRecyclerViewAdapter = new PostRecyclerViewAdapter(feed.getFeedAsList());
        feedRecyclerView.setAdapter(postRecyclerViewAdapter);

        OverScrollDecoratorHelper.setUpOverScroll(feedRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // Note: over-scroll is set-up using the helper method.
        IOverScrollDecor decor = OverScrollDecoratorHelper.setUpOverScroll(feedRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        decor.setOverScrollStateListener(new IOverScrollStateListener() {
                                             @Override
                                             public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
                                                 switch (newState) {
                                                     case STATE_IDLE:
                                                         break;
                                                     case STATE_DRAG_START_SIDE:
                                                         getNewerPostsEvent();
                                                         break;
                                                     case STATE_DRAG_END_SIDE:
                                                         getOlderPostsEvent();
                                                         break;
                                                     case STATE_BOUNCE_BACK:
                                                         if (oldState == STATE_DRAG_START_SIDE) {
                                                         } else {
                                                         }
                                                         break;
                                                 }
                                             }
                                         });


        feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (scrollPosition == 0 && maxScrollPosition != 0 && !hasGottenNewerPosts){
                    getNewerPostsEvent();
                    hasGottenNewerPosts = true;
                } else if (scrollPosition == maxScrollPosition && !hasIncreasedMaxScrollPosition && !hasGottenOlderPosts) {
                    getOlderPostsEvent();
                    hasGottenOlderPosts = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollPosition += dy;
                if (scrollPosition > maxScrollPosition){
                    maxScrollPosition = scrollPosition;
                    hasIncreasedMaxScrollPosition = true;
                }
                else if (scrollPosition == maxScrollPosition){
                    // do nothing ?
                }
                else {
                    hasIncreasedMaxScrollPosition = false;
                    hasGottenOlderPosts = false;
                    hasGottenNewerPosts = false;
                }
                if (debug){
                    Log.d("FeedFragment", "onScrolled dx: " + dx + " dy: " + dy);
                    Log.d("FeedFragment", "ScrollPosition " + scrollPosition + " / " + maxScrollPosition);
                }

            }
        });
    }

    private void getNewerPostsEvent(){
        Toast.makeText(getContext(),"Looking for newer posts...",Toast.LENGTH_SHORT).show();
        feed.updateWithNewerPosts();
        postRecyclerViewAdapter.notifyDataSetChanged();
//        feed.updateWithOlderPosts();
//        postRecyclerViewAdapter.updatePosts(feed.getFeedAsList());
    }

    private void getOlderPostsEvent(){
        Toast.makeText(getContext(),"Looking for older posts...",Toast.LENGTH_SHORT).show();
        feed.updateWithOlderPosts();
        postRecyclerViewAdapter.notifyDataSetChanged();
//        feed.updateWithOlderPosts();Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();
//        postRecyclerViewAdapter.updatePosts(feed.getFeedAsList());
    }

    @Override
    public void update(Observable o, Object arg) {
        postRecyclerViewAdapter.notifyDataSetChanged();
    }

}
