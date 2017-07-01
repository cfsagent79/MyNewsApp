package com.example.gabriele.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {


        /**
         * Constant value for the news loader ID
         */
        private static final int NEWS_LOADER_ID = 1;

        public static final String LOG_TAG = MainActivity.class.getName();

        /**
         * URL for news data from The Guardian API
         */
        private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?";

        /**
         * Adapter for the list of news
         */
        private NewsAdapter mNewsAdapter;

        /**
         * SearchView that takes the query
         */
        private SearchView searchView;

        /**
         * List of news
         */
        private ListView newsListView;

        /**
         * Value for search query
         */
        private String mQuery;

        /**
         * TextView that is displayed when there's no news in the list
         */
        private TextView mEmptyStateTextView;

        /**
         * ProgressBar that is displayed while the data is loading
         */
        private ProgressBar mProgressBar;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Find a reference to the {@link SearchView} in the layout
            searchView = (SearchView) findViewById(R.id.search_view);

            // Find a reference to the {@link ListView} in the layout
            newsListView = (ListView) findViewById(R.id.news_list);

            // Create a new adapter that takes an empty list of news as input
            mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            newsListView.setAdapter(mNewsAdapter);

            // Set an item click listener on the ListView, which sends an intent to a web browser
            // to open a website with more information about the selected book.
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    // Find the current book that was clicked on
                    News currentNews = mNewsAdapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri newsUri = Uri.parse(currentNews.getNewsUrl());

                    // Create a new intent to view the news URI
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                    // Send the intent to launch a new activity
                    startActivity(webIntent);
                }
            });

// Find the reference to the progress bar in a layout
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
            // Find the reference to the empty text view in a layout and set empty view
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            newsListView.setEmptyView(mEmptyStateTextView);


            if (isConnected()) {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            } else {
                mProgressBar.setVisibility(View.GONE);
                mEmptyStateTextView.setText(R.string.no_internet);
            }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (isConnected()) {
                        newsListView.setVisibility(View.INVISIBLE);
                        mEmptyStateTextView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mQuery = searchView.getQuery().toString();
                        mQuery = mQuery.replace(" ", "+");
                        Log.v(LOG_TAG, mQuery);
                        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                        searchView.clearFocus();
                    } else {
                        newsListView.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        mEmptyStateTextView.setText(R.string.no_internet);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        // Helper method to check network connection
        public boolean isConnected() {
            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // Get details on the currently active default data network
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            // If there is a network connection, fetch data
            return (networkInfo != null && networkInfo.isConnected());
        }

        @Override
        public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
            String requestUrl = "";
            if (mQuery != null && mQuery != "") {
                requestUrl = NEWS_REQUEST_URL + mQuery;
            } else {
                String defaultQuery = "android";
                requestUrl = NEWS_REQUEST_URL + defaultQuery;
            }
            return new NewsLoader(this, requestUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
            // Set empty state text to display "No news found."
            mEmptyStateTextView.setText(R.string.no_news);
            // Hide loading indicator because the data has been loaded
            mProgressBar.setVisibility(View.GONE);
            // Clear the adapter of previous news data
            mNewsAdapter.clear();

            // If there is a valid list of {@link News}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (news != null && !news.isEmpty()) {
                mNewsAdapter.addAll(news);
            }
        }

        // Loader reset, so we can clear out our existing data.
        @Override
        public void onLoaderReset(Loader<List<News>> loader) {
            mNewsAdapter.clear();
        }


    }
