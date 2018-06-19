package com.georgcantor.wallpaperapp.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.georgcantor.wallpaperapp.R;
import com.georgcantor.wallpaperapp.model.Hit;
import com.georgcantor.wallpaperapp.model.WallDownloadTable;
import com.georgcantor.wallpaperapp.network.NetworkUtilities;
import com.georgcantor.wallpaperapp.ui.adapter.TagAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PicDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PIC = "picture";
    public static final String origin = "caller";
    private Hit hit;
    private List<String> tags = new ArrayList<String>();
    int first = 0;
    public NetworkUtilities networkUtilities;
    public RecyclerView recyclerView;
    public TagAdapter tagAdapter;
    public boolean isDownloaded = false;
    public boolean isCallerCollection = false;
    private Menu menu;
    private File file;
    private int permissionCheck1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkUtilities = new NetworkUtilities(this);
        setContentView(R.layout.activity_pic_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        TextView tagTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        permissionCheck1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (getIntent().hasExtra(EXTRA_PIC)) {
            hit = getIntent().getParcelableExtra(EXTRA_PIC);
        } else {
            throw new IllegalArgumentException("Detail activity must receive a Hit parcelable");
        }
        String title = hit.getTags();
        while (title.contains(",")) {
            String f = title.substring(0, title.indexOf(","));
            tags.add(f);
            first = title.indexOf(",");
            title = title.substring(++first);
        }
        tags.add(title);
        tagTitle.setText(tags.get(0));
        ImageView wallp = findViewById(R.id.wallpaper_detail);
        TextView fav = findViewById(R.id.fav);
        TextView userId = findViewById(R.id.user_name);
        ImageView userImage = findViewById(R.id.user_image);
        TextView downloads = findViewById(R.id.down);
        recyclerView = findViewById(R.id.tagsRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        tagAdapter = new TagAdapter(this);
        tagAdapter.setTagList(tags);
        recyclerView.setAdapter(tagAdapter);
        file = new File(Environment.getExternalStoragePublicDirectory("/"
                + getResources().getString(R.string.app_name)), hit.getId()
                + getResources().getString(R.string.jpg));

        if (getIntent().hasExtra(origin)) {
            Picasso.with(this)
                    .load(file)
                    .placeholder(R.drawable.plh)
                    .into(wallp);
            isCallerCollection = true;
        } else {
            Picasso.with(this)
                    .load(hit.getWebformatURL())
                    .placeholder(R.drawable.plh)
                    .into(wallp);
        }
        userId.setText(hit.getUser());
        downloads.setText(String.valueOf(hit.getDownloads()));
        fav.setText(String.valueOf(hit.getFavorites()));
        if (!networkUtilities.isInternetConnectionPresent()) {
            Picasso.with(this)
                    .load(R.drawable.memb)
                    .transform(new CropCircleTransformation())
                    .into(userImage);
        } else {
            if (!hit.getUserImageURL().isEmpty()) {
                Picasso.with(this)
                        .load(hit.getUserImageURL())
                        .transform(new CropCircleTransformation())
                        .into(userImage);
            } else {
                Picasso.with(this)
                        .load(R.drawable.memb)
                        .transform(new CropCircleTransformation())
                        .into(userImage);
            }
        }
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isCallerCollection) {
            this.menu = menu;
            getMenuInflater().inflate(R.menu.menu_details, menu);
        } else {
            this.menu = menu;
            getMenuInflater().inflate(R.menu.menu_details_collection, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_down) {
            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
                if (!fileExistance()) {
                    String uri = hit.getWebformatURL();
                    uri = uri.replaceAll("_640", "_960");
                    Uri image_uri = Uri.parse(uri);
                    downloadData(image_uri);
                    item.setEnabled(false);
                } else {
                    Toast toast = Toast.makeText(this, getResources()
                            .getString(R.string.image_downloaded), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                checkPermisson();
            }
        }
        if (item.getItemId() == R.id.action_set) {
            if (fileExistance()) {
                Uri sendUri2 = Uri.fromFile(file);
                Log.d(getResources().getString(R.string.URI), sendUri2.toString());
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(sendUri2, getResources().getString(R.string.image_jpg));
                intent.putExtra(getResources().getString(R.string.mimeType),
                        getResources().getString(R.string.image_jpg));
                startActivityForResult(Intent.createChooser(intent,
                        getResources().getString(R.string.Set_As)), 200);
            } else {
                Toast toast = Toast.makeText(this,
                        getResources().getString(R.string.first_down), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private long downloadData(Uri uri) {
        long downloadReference;
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        String name = Environment.getExternalStorageDirectory().getAbsolutePath();
        name += "/YourDirectoryName/";

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(tags.get(0) + getResources().getString(R.string.down));
        request.setDescription(getResources().getString(R.string.down_canvas));
        request.setDestinationInExternalPublicDir("/"
                + getResources().getString(R.string.app_name), hit.getId()
                + getResources().getString(R.string.jpg));
        downloadReference = downloadManager.enqueue(request);
        getContentResolver().insert(WallDownloadTable.CONTENT_URI,
                WallDownloadTable.getContentValues(hit, false));
        getContentResolver().notifyChange(WallDownloadTable.CONTENT_URI, null);

        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast toast = Toast.makeText(context, tags.get(0) + getResources()
                    .getString(R.string.down_complete), Toast.LENGTH_SHORT);
            toast.show();
            isDownloaded = true;
            MenuItem menuItem = menu.findItem(R.id.action_down);
            menuItem.setEnabled(true);
        }
    };

    @Override
    public void onDestroy() {
        try {
            if (downloadReceiver != null)
                unregisterReceiver(downloadReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public boolean fileExistance() {
        return file.exists();
    }

    public void checkPermisson() {
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 102;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }
}
