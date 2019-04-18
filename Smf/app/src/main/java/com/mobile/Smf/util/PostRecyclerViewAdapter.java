package com.mobile.Smf.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.Smf.R;
import com.mobile.Smf.model.PicturePost;
import com.mobile.Smf.model.Post;
import com.mobile.Smf.model.TextPost;

import java.util.List;

/*
* Adapter for multiple types of Posts, inspired by Gilbert Christopher's implmentation found at:
* https://medium.com/@gilbertchristopher/a-recyclerview-with-multiple-view-type-22619a5ad365
* */

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.GenericViewHolder> {

    private static final int TYPE_TEXT_POST = 1;
    private static final int TYPE_PICTURE_POST = 2;

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

        public TextPostViewHolder(View itemView){
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.view_textpost_textview_username);
            textViewTimestamp = (TextView) itemView.findViewById(R.id.view_textpost_textview_timestamp);
            textViewText = (TextView) itemView.findViewById(R.id.view_textpost_textview_text);
        }

        @Override
        public void bind(TextPost tPost){
            textViewUsername.setText(tPost.getUserName());
            textViewTimestamp.setText(tPost.getTimeStampAsStr());
            textViewText.setText(tPost.getText());
        }
    }

    public static class PicturePostHolder extends GenericViewHolder<PicturePost> {

        private TextView textViewUsername;
        private TextView textViewTimestamp;
        private ImageView imageViewPicture;

        public PicturePostHolder(View itemView){
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.view_picturepost_textview_username);
            textViewTimestamp = (TextView) itemView.findViewById(R.id.view_picturepost_textview_timestamp);
            imageViewPicture = (ImageView) itemView.findViewById(R.id.view_picturepost_imageview_picture);
        }

        @Override
        public void bind(PicturePost pPost){
            textViewUsername.setText(pPost.getUserName());
            textViewTimestamp.setText(pPost.getTimeStampAsStr());
            imageViewPicture.setImageBitmap(pPost.getPicture());
        }
    }

}
