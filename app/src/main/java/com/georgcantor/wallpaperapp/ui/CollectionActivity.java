package com.georgcantor.wallpaperapp.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.georgcantor.wallpaperapp.R;
import com.georgcantor.wallpaperapp.model.Hit;
import com.georgcantor.wallpaperapp.model.WallDownloadTable;
import com.georgcantor.wallpaperapp.network.NetworkUtilities;
import com.georgcantor.wallpaperapp.ui.adapter.CollectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    public CollectionAdapter collectionAdapter;
    public RecyclerView recyclerView_coll;
    public NetworkUtilities networkUtilities;
    public List<Hit> result;
    public static final int LOADER_ID = 1;
    public int column_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkUtilities = new NetworkUtilities(this);
        setContentView(R.layout.activity_select_category);
        Toolbar toolbar = findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.collection_string);
        recyclerView_coll = findViewById(R.id.SelCatRecView);
        recyclerView_coll.setHasFixedSize(true);

        checkScreenSize();
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(column_no, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_coll.setLayoutManager(staggeredGridLayoutManager);
        collectionAdapter = new CollectionAdapter(this);

        getSupportLoaderManager().initLoader(LOADER_ID, null,
                (android.support.v4.app.LoaderManager.LoaderCallbacks<Object>) contactsLoader);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    return new CursorLoader(CollectionActivity.this,
                            WallDownloadTable.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    result = WallDownloadTable.getRows(cursor, true);
                    collectionAdapter.setHitList(result);
                    recyclerView_coll.setAdapter(collectionAdapter);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    result = new ArrayList<>();
                    collectionAdapter.setHitList(result);
                    recyclerView_coll.setAdapter(collectionAdapter);
                }
            };

    public void checkScreenSize() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                column_no = 4;
                break;
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                column_no = 3;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                column_no = 3;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                column_no = 2;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                column_no = 2;
                break;
            default:
                column_no = 2;
        }
    }
}