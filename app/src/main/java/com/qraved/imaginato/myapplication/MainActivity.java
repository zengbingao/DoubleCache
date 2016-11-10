package com.qraved.imaginato.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    /**
     * 用于展示照片墙的GridView 
     */
    private GridView mPhotoWall;

    /**
     * GridView的适配器 
     */
    private PhotoWallAdapter mAdapter;

    private int mImageThumbSize;
    private int mImageThumbSpacing;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("robin",Runtime.getRuntime().maxMemory()/1024/1024+"M");
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        mPhotoWall = (GridView) findViewById(R.id.photo_wall);
        mAdapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls,
                mPhotoWall);
        mPhotoWall.setAdapter(mAdapter);
        mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(mPhotoWall
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            int columnWidth = (mPhotoWall.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            mAdapter.setItemHeight(columnWidth);
                            mPhotoWall.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        String a1= null;
        try {
            a1 = Environment.getDataDirectory().getCanonicalPath()+"\n"+Environment.getDownloadCacheDirectory().getCanonicalPath()+"\n"+Environment.getExternalStorageDirectory().getPath()
                    +"\n"+Environment.getRootDirectory().getPath()+"\n"
                    +Environment.getExternalStorageState()+"\n"+Environment.isExternalStorageEmulated()+"\n"+Environment.isExternalStorageRemovable()+"\n"+"\n"+"\n";
        } catch (IOException e) {
            e.printStackTrace();
        }
        String a2=this.getExternalFilesDir("")+"\n"+this.getExternalCacheDir()+"\n"+this.getFilesDir()+"\n"+this.getCacheDir()+"\n"+this.getObbDir()+"\n";
        Log.i("robin",a1+a2);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.fluchCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务  
        mAdapter.cancelAllTasks();
    }
}
