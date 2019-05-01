package com.mobile.Smf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobile.Smf.util.PostRecyclerViewAdapter;
import com.mobile.Smf.R;
import com.mobile.Smf.model.Feed;

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

        feed = getFeedSingleton(getContext());

        feedRecyclerView = (RecyclerView) feedView.findViewById(R.id.feed_recyclerview);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        startRecyclerView();

        return feedView;
    }

    private void startRecyclerView() {
        postRecyclerViewAdapter = new PostRecyclerViewAdapter(feed.getFeedAsList());
        feedRecyclerView.setAdapter(postRecyclerViewAdapter);

        /*
        * Uses the excellent iverscroll decor implementation by 'EverythingMe'
        * found @ https://github.com/EverythingMe/overscroll-decor
        * */
        OverScrollDecoratorHelper.setUpOverScroll(feedRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

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

    }

    private void getNewerPostsEvent(){
        Toast.makeText(getContext(),"Looking for newer posts...",Toast.LENGTH_SHORT).show();
        feed.updateWithNewerPosts();
        postRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getOlderPostsEvent(){
        Toast.makeText(getContext(),"Looking for older posts...",Toast.LENGTH_SHORT).show();
        feed.updateWithOlderPosts();
        postRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
        postRecyclerViewAdapter.notifyDataSetChanged();
    }

}
