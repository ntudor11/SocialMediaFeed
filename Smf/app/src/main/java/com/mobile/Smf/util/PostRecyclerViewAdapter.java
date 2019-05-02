package com.mobile.Smf.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.Smf.R;
import com.mobile.Smf.fragments.FeedFragment;
import com.mobile.Smf.model.Feed;
import com.mobile.Smf.model.PicturePost;
import com.mobile.Smf.model.Post;
import com.mobile.Smf.model.TextPost;

import java.util.List;

/*
* Adapter for multiple types of Posts, inspired by Gilbert Christopher's implmentation found at:
* https://medium.com/@gilbertchristopher/a-recyclerview-with-multiple-view-type-22619a5ad365
* */

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.GenericViewHolder> {

    private static final int TYPE_TEXT_POST = 0;
    private static final int TYPE_PICTURE_POST = 1;

    private boolean hasGottenNewPosts = true;

    private List<Post> posts;

    public PostRecyclerViewAdapter(List<Post> newPosts){
        posts = newPosts;
    }

    public void updatePosts(List<Post> newPosts){
        posts = newPosts;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType){
            case TYPE_TEXT_POST: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_post_textpost,parent,false);
                return new TextPostViewHolder(view);
            }
            case TYPE_PICTURE_POST: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_post_picturepost,parent,false);
                return new PicturePostHolder(view);
            }
            default: throw new IllegalArgumentException("Invalid post type.");
        }
    }

    @Override
    public int getItemViewType(int position){
        return posts.get(position).getPostType();
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position){
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount(){
        return posts.size();
    }

    //      >>> VIEW HOLDERS <<<

    public static abstract class GenericViewHolder<T> extends RecyclerView.ViewHolder {
        private GenericViewHolder(View itemView){
            super(itemView);
        }
        public abstract void bind(T type);
    }

    public static class TextPostViewHolder extends GenericViewHolder<TextPost> {

        private TextView textViewUsername;
        private TextView textViewTimestamp;
        private TextView textViewText;
        private ImageView like;
        private TextView numberOfLikes;
        private final ItemBinding binding;

        public TextPostViewHolder(View itemView){
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.view_textpost_textview_username);
            textViewTimestamp = (TextView) itemView.findViewById(R.id.view_textpost_textview_timestamp);
            textViewText = (TextView) itemView.findViewById(R.id.view_textpost_textview_text);
            like = (ImageView) itemView.findViewById(R.id.view_post_imageview_like_not);
            numberOfLikes = (TextView) itemView.findViewById(R.id.view_post_textview_numlikes);
        }

        @Override
        public void bind(TextPost tPost){
            //if the post is broken we do not show it
            if (tPost == null){
                return;
            }
            textViewUsername.setText(tPost.getUserName());
            textViewTimestamp.setText(tPost.getUniversalTimeStamp());
            textViewText.setText(tPost.getText());
            numberOfLikes.setText(""+tPost.getlikes());
            if(tPost.getClicked())
                like.setImageResource(R.drawable.liked);
            else
                like.setImageResource(R.drawable.liked_not);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!tPost.getClicked()) {
                        like.setImageResource(R.drawable.liked);
                        tPost.likePost();
                        numberOfLikes.setText(""+(tPost.getlikes()));
                        System.out.println("like.clicklist -> post with id: " + tPost.getPostID() + " and userName: "
                                + tPost.getUserName() + " liked - number of likes = "+tPost.getlikes()+" updateVal = "+tPost.getUpdateVal()+" isClicked ="+tPost.getClicked());
                        Log.d("like.clicklist", "post with id: " + tPost.getPostID() + " and userName: " + tPost.getUserName() + " liked");
                    } else {
                        like.setImageResource(R.drawable.liked_not);
                        tPost.unlikePost();
                        numberOfLikes.setText(""+(tPost.getlikes()));
                        System.out.println("UNlike.clicklist -> post with id: " + tPost.getPostID() + " and userName: " + tPost.getUserName() +
                                " liked- number of likes = "+tPost.getlikes()+" updateVal = "+tPost.getUpdateVal()+" isClicked ="+tPost.getClicked());
                        Log.d("like.clicklist", "post with id: " + tPost.getPostID() + " and userName: " + tPost.getUserName() + " UNliked");

                    }
                }
            });
        }
    }

    public static class PicturePostHolder extends GenericViewHolder<PicturePost> {

        private TextView textViewUsername;
        private TextView textViewTimestamp;
        private ImageView imageViewPicture;
        private ImageView like;
        private TextView numberOfLikes;

        public PicturePostHolder(View itemView){
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.view_picturepost_textview_username);
            textViewTimestamp = (TextView) itemView.findViewById(R.id.view_picturepost_textview_timestamp);
            imageViewPicture = (ImageView) itemView.findViewById(R.id.view_picturepost_imageview_picture);
            like = (ImageView) itemView.findViewById(R.id.view_post_imageview_like_not);
            numberOfLikes = (TextView) itemView.findViewById(R.id.view_post_textview_numlikes);
        }

        @Override
        public void bind(PicturePost pPost){
            //if the post is broken we do not show it
            if (pPost.getPicture() == null){
                return;
            }
            textViewUsername.setText(pPost.getUserName());
            textViewTimestamp.setText(pPost.getUniversalTimeStamp());
            imageViewPicture.setImageBitmap(pPost.getPicture());
            numberOfLikes.setText(""+pPost.getlikes());
            if(pPost.getClicked())
                like.setImageResource(R.drawable.liked);
            else
                like.setImageResource(R.drawable.liked_not);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!pPost.getClicked()) {
                        like.setImageResource(R.drawable.liked);
                        pPost.likePost();
                        numberOfLikes.setText(""+pPost.getlikes());

                        System.out.println("like.clicklist -> post with id: " + pPost.getPostID() + " and userName: " + pPost.getUserName() + " liked");
                        Log.d("like.clicklist","post with id: "+pPost.getPostID()+" and userName: "+pPost.getUserName()+" liked");
                    } else {
                        like.setImageResource(R.drawable.liked_not);
                        pPost.unlikePost();
                        numberOfLikes.setText(""+pPost.getlikes());

                        System.out.println("UNlike.clicklist -> post with id: " + pPost.getPostID() + " and userName: " + pPost.getUserName() + " liked");
                        Log.d("like.clicklist","post with id: "+pPost.getPostID()+" and userName: "+pPost.getUserName()+" UNliked");

                    }
                }
            });
        }
    }

}
