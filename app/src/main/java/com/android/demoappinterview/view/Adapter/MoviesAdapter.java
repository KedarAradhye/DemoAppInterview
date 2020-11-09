package com.android.demoappinterview.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.demoappinterview.Model.GetSearchResultResponse;
import com.android.demoappinterview.Model.Search;
import com.android.demoappinterview.R;
import com.android.demoappinterview.view.Activity.MainActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context mContext;
    private List<Search> mSearchResultList = new ArrayList<>();

    public MoviesAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_layout, parent, false);
        MoviesAdapter.ViewHolder viewHolder = new MoviesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        mSearchResultList.get(position);
        Glide.with(mContext).load(mSearchResultList.get(position).getPoster()).into(holder.poster);
        holder.title.setText(mSearchResultList.get(position).getTitle());
        holder.type.setText(mSearchResultList.get(position).getType());
        holder.year.setText(mSearchResultList.get(position).getYear());


    }

    @Override
    public int getItemCount() {
        return mSearchResultList.size();
    }

    public void setSearchResult(List<Search> searchResultList) {
        mSearchResultList = searchResultList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView title;
        public TextView type;
        public TextView year;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.iv_poster);
            title = itemView.findViewById(R.id.tv_title_text);
            type = itemView.findViewById(R.id.tv_type_text);
            year = itemView.findViewById(R.id.tv_year_text);

        }
    }

}
