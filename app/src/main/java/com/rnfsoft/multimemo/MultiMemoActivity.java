package com.rnfsoft.multimemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rnfsoft.multimemo.common.TitleBitmapButton;
import com.rnfsoft.multimemo.db.MemoDatabase;

import java.io.File;

public class MultiMemoActivity extends Activity {

    private static final String TAG = "MultiMemoActivity" ;
    private ListView mMemoListView;
    private MemoListAdapter mMemoListAdapter;

    public static MemoDatabase mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimemo);

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "No SD Card, Please enter SD card", Toast.LENGTH_LONG).show();
            return;
        } else {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!BasicInfo.ExternalChecked && externalPath != null) {
                BasicInfo.ExternalPath = externalPath + File.separator;
                Log.d(TAG, "ExternalPath : " + BasicInfo.ExternalPath);

                BasicInfo.FOLDER_PHOTO = BasicInfo.ExternalPath + BasicInfo.FOLDER_PHOTO;
                BasicInfo.FOLDER_VIDEO = BasicInfo.ExternalPath + BasicInfo.FOLDER_VIDEO;
                BasicInfo.FOLDER_VOICE = BasicInfo.ExternalPath + BasicInfo.FOLDER_VOICE;
                BasicInfo.FOLDER_HANDWRITING = BasicInfo.ExternalPath + BasicInfo.FOLDER_HANDWRITING;
                BasicInfo.DATABASE_NAME = BasicInfo.ExternalPath + BasicInfo.DATABASE_NAME;

                BasicInfo.ExternalChecked = true;
            }
        }



        mMemoListView = (ListView)findViewById(R.id.memoList);
        mMemoListAdapter = new MemoListAdapter(this);
        mMemoListView.setAdapter(mMemoListAdapter);
        mMemoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                viewMemo(position);
            }
        });


        TitleBitmapButton newMemoBtn = (TitleBitmapButton) findViewById(R.id.newMemoBtn);
        newMemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "newMemoBtn clicked");

                Intent intent = new Intent(getApplicationContext(), MemoInsertActivity.class);
                intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_INSERT);
                startActivityForResult(intent, BasicInfo.REQ_INSERT_ACTIVITY);
            }
        });

        TitleBitmapButton closeBtn = (TitleBitmapButton) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    private void viewMemo(int position){
        MemoListItem item = (MemoListItem)mMemoListAdapter.getItem(position);

        Intent intent = new Intent(getApplicationContext(), MemoInsertActivity.class);
        intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);
        intent.putExtra(BasicInfo.KEY_MEMO_ID, item.getId());
        intent.putExtra(BasicInfo.KEY_MEMO_DATE, item.getData(0));
        intent.putExtra(BasicInfo.KEY_MEMO_TEXT, item.getData(1));

        intent.putExtra(BasicInfo.KEY_ID_HANDWRITING, item.getData(2));
        intent.putExtra(BasicInfo.KEY_URI_HANDWRITING, item.getData(3));

        intent.putExtra(BasicInfo.KEY_ID_PHOTO, item.getData(4));
        intent.putExtra(BasicInfo.KEY_URI_PHOTO, item.getData(5));

        intent.putExtra(BasicInfo.KEY_ID_VIDEO, item.getData(6));
        intent.putExtra(BasicInfo.KEY_URI_VIDEO, item.getData(7));

        intent.putExtra(BasicInfo.KEY_ID_VOICE, item.getData(8));
        intent.putExtra(BasicInfo.KEY_URI_VOICE, item.getData(9));

        startActivityForResult(intent, BasicInfo.REQ_VIEW_ACTIVITY);






    }

    @Override
    protected void onStart() {

        openDatabase();
        loadMemoListData();
        super.onStart();
    }

    private void openDatabase() {
        if( mDatabase != null)
        {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = MemoDatabase.getInstance(this);
        boolean isOpen = mDatabase.open();

        if(isOpen){
            Log.d(TAG, "Memo database is open.");
        } else {
            Log.d(TAG, "Memo database is not open");
        }
    }


    private void loadMemoListData() {
        String SQL = "select _id, INPUT_DATE, CONTENT_TEXT, ID_PHOTO, ID_VIDEO, ID_VOICE, ID_HANDWRITING from MEMO order by INPUT_DATE desc";

        int recordCount = -1;

        if(mDatabase != null){
            Cursor outCursor = mDatabase.rawQuery(SQL);

            recordCount = outCursor.getCount();
            Log.d(TAG, "cursor count : " + recordCount + "\n");

            mMemoListAdapter.clear();
            Resources res = getResources();

            for(int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                String memoId = outCursor.getString(0);

                String dateStr = outCursor.getString(1);
                if(dateStr.length() > 10){
                    dateStr.substring(0,10);
                }

                String memoStr = outCursor.getString(2);
                String photoId = outCursor.getString(3);
                String photoUriStr = getPhotoUriStr(photoId);

                String videoId = outCursor.getString(4);
                String videoUriStr = null;

                String voiceId = outCursor.getString(5);
                String voiceUriStr = null;

                String handwritingId = outCursor.getString(6);
                String handwritingUriStr = null;

                mMemoListAdapter.addItem(new MemoListItem(memoId, dateStr, memoStr, handwritingId, handwritingUriStr, photoId, photoUriStr, videoId, videoUriStr, voiceId, voiceUriStr));
            }
            outCursor.close();
            mMemoListAdapter.notifyDataSetChanged();
        }

    }

    public String getPhotoUriStr(String id_photo){
        String photoUriStr = null;

        if(id_photo != null && !id_photo.equals("-1"))
        {
            String SQL = "SELECT URI FROM " + MemoDatabase.TABLE_PHOTO + " WHERE _ID = " + id_photo + "";
            Cursor photoCursor = mDatabase.rawQuery(SQL);
            if(photoCursor.moveToNext()){
                photoUriStr = photoCursor.getString(0);
            }
            photoCursor.close();
        } else if(id_photo == null || id_photo.equals("-1")){
            photoUriStr = "";
        }
        return photoUriStr;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case BasicInfo.REQ_INSERT_ACTIVITY:
                if(resultCode == RESULT_OK){
                    loadMemoListData();
                }
                break;
            case BasicInfo.REQ_VIEW_ACTIVITY:
                loadMemoListData();
                break;
        }
    }
}
