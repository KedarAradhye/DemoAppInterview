package com.android.demoappinterview.view.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.demoappinterview.Model.GetSearchResultResponse;
import com.android.demoappinterview.Model.Search;
import com.android.demoappinterview.Network.ApiClient;
import com.android.demoappinterview.Network.ApiInterface;
import com.android.demoappinterview.R;
import com.android.demoappinterview.Utils.Constants;
import com.android.demoappinterview.view.Adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMovieRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MoviesAdapter mAdapter;
    private int mPageNumber = 1;
    private List<Search> mSearchResultList = new ArrayList<>();
    private Boolean isScrolling = false;
    private int mCurrentItems, mTotalItems, mScrolledOutItems;
    private String mSearchName = "batman";
    private SearchView mSearchView;
    private TextView mNoMatchFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
        SearchList(mSearchName,mPageNumber);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPageNumber = 1;
                mSearchName = query;
               SearchList(query,mPageNumber);
               return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                SearchList(newText);

                return false;
            }
        });


        mMovieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mCurrentItems = mLayoutManager.getChildCount();
                mTotalItems = mLayoutManager.getItemCount();
                mScrolledOutItems = mLayoutManager.findFirstVisibleItemPosition();

                if ((isScrolling && (mCurrentItems + mScrolledOutItems == mTotalItems))) {
                    //                     mFragmentBinding.rotateloading.start();
                    isScrolling = false;
                    mPageNumber++;
                    SearchList(mSearchName,mPageNumber);
                }
            }
        });

    }

    private void SearchList(String searchname,int pagenumber) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetSearchResultResponse> call = apiInterface.getSearchList(searchname, pagenumber, Constants.API_KEY);

        call.enqueue(new Callback<GetSearchResultResponse>() {
            @Override
            public void onResponse(Call<GetSearchResultResponse> call, Response<GetSearchResultResponse> response) {
                if (response.body() != null) {
                    if (response.body().getResponse().equals("True")) {
                        onSearchResultSuccess(response.body());
                    } else {
                        onSearchResultFailed(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSearchResultResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void onSearchResultFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mNoMatchFound.setVisibility(View.VISIBLE);
        mMovieRecyclerView.setVisibility(View.GONE);
    }

    private void onSearchResultSuccess(GetSearchResultResponse body) {
        if (body.getSearch() != null) {
            if (mPageNumber == 1) {
                mSearchResultList.clear();
                mSearchResultList.addAll(body.getSearch());
                mAdapter.setSearchResult(mSearchResultList);
            } else {
                mSearchResultList.addAll(body.getSearch());
                mAdapter.setSearchResult(mSearchResultList);
            }
            mNoMatchFound.setVisibility(View.GONE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);
        }/*else{
            mNoMatchFound.setVisibility(View.VISIBLE);
            mMovieRecyclerView.setVisibility(View.GONE);
        }*/
    }

    private void initviews() {
        mMovieRecyclerView = findViewById(R.id.rv_moviewshow);
        mSearchView = findViewById(R.id.sv_movie_search);
        mNoMatchFound = findViewById(R.id.tv_no_match_found);
        mLayoutManager = (new LinearLayoutManager(this));
        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setNestedScrollingEnabled(false);
        mMovieRecyclerView.setItemAnimator(null);
        mAdapter = new MoviesAdapter(this);
        mMovieRecyclerView.setAdapter(mAdapter);

    }
}