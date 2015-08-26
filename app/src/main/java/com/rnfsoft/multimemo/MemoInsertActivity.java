package com.rnfsoft.multimemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.rnfsoft.multimemo.common.TitleBackgroundButton;
import com.rnfsoft.multimemo.common.TitleBitmapButton;
import com.rnfsoft.multimemo.db.MemoDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tchoi on 8/17/2015.
 */
public class MemoInsertActivity extends Activity {
    private static final String TAG = "MemoInsertActivity" ;
    TitleBackgroundButton titleBackGroundBtn, insert_textBtn, insert_handwritingBtn;
    TitleBitmapButton insertSaveBtn, insertCancelBtn, insertDateButton;
    ImageView mPhoto;
    EditText mMemoEdit, insert_memoEdit;
    View insert_handwriting;
    String mMemoMode, mMemoId, mMemoDate, mDateStr, mMemoStr, mMediaPhotoUri, mMediaPhotoId, mMediaVideoId, mMediaVideoUri, mMediaVoiceId, mMediaVoiceUri, mMediaHandwritingId, mMediaHandwritingUri;

    Animation translateLeftAnim, translateRightAnim;

    boolean isPhotoCaptured, isPhotoFileSaved, isPhotoCanceled;

    Calendar mCalendar = Calendar.getInstance();
    Bitmap resultPhotoBitmap;
    int textViewMode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_insert_activity);

        titleBackGroundBtn = (TitleBackgroundButton) findViewById(R.id.titleBackgroundBtn);
        mPhoto = (ImageView)findViewById(R.id.insert_photo);
        mMemoEdit = (EditText)findViewById(R.id.insert_memoEdit);

        insert_textBtn = (TitleBackgroundButton)findViewById(R.id.insert_textBtn);
        insert_handwritingBtn = (TitleBackgroundButton)findViewById(R.id.insert_handwritingBtn);
        insert_memoEdit = (EditText)findViewById(R.id.insert_memoEdit);
        insert_handwriting = (View)findViewById(R.id.insert_handwriting);

        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

        insert_textBtn.setSelected(true);
        insert_handwritingBtn.setSelected(false);

        insert_textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textViewMode == 1) {
                    insert_handwriting.setVisibility(View.GONE);
                    insert_memoEdit.setVisibility(View.VISIBLE);
                    insert_memoEdit.startAnimation(translateLeftAnim);

                    textViewMode = 0;
                    insert_textBtn.setSelected(true);
                    insert_handwritingBtn.setSelected(false);
                }
            }
        });

        insert_handwritingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewMode == 0) {
                    insert_handwriting.setVisibility(View.VISIBLE);
                    insert_memoEdit.setVisibility(View.GONE);
                    insert_memoEdit.startAnimation(translateLeftAnim);

                    textViewMode = 1;
                    insert_textBtn.setSelected(false);
                    insert_handwritingBtn.setSelected(true);
                }
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPhotoCaptured || isPhotoFileSaved) {
                    showDialog(BasicInfo.CONTENT_PHOTO_EX);
                } else {
                    showDialog(BasicInfo.CONTENT_PHOTO);
                }

            }
        });
        
        
        setBottomButtons();
        
        setMediaLayout();
        
        setCalendar();

        Intent intent = getIntent();
        mMemoMode = intent.getStringExtra(BasicInfo.KEY_MEMO_MODE);
        if(mMemoMode.equals(BasicInfo.MODE_MODIFY) || mMemoMode.equals(BasicInfo.MODE_VIEW)){
            processIntent(intent);
            titleBackGroundBtn.setText("Review Memo");
            insertSaveBtn.setText("Edit");

        }else {
            titleBackGroundBtn.setText("New Memo");
            insertSaveBtn.setText("Save");
        }


    }

    private void processIntent(Intent intent) {
        mMemoId = intent.getStringExtra(BasicInfo.KEY_MEMO_ID);
        mMemoEdit.setText(intent.getStringExtra(BasicInfo.KEY_MEMO_TEXT));
        mMediaPhotoId = intent.getStringExtra(BasicInfo.KEY_ID_PHOTO);
        mMediaPhotoUri = intent.getStringExtra(BasicInfo.KEY_URI_PHOTO);
        mMediaVideoId = intent.getStringExtra(BasicInfo.KEY_ID_VIDEO);
        mMediaVideoUri = intent.getStringExtra(BasicInfo.KEY_URI_VIDEO);
        mMediaVoiceId = intent.getStringExtra(BasicInfo.KEY_ID_VOICE);
        mMediaVoiceUri = intent.getStringExtra(BasicInfo.KEY_URI_VOICE);
        mMediaHandwritingId = intent.getStringExtra(BasicInfo.KEY_ID_HANDWRITING);
        mMediaHandwritingUri = intent.getStringExtra(BasicInfo.KEY_URI_HANDWRITING);

        setMediaImage(mMediaPhotoId, mMediaPhotoUri, mMediaVideoId, mMediaVoiceId, mMediaHandwritingId);
    }

    private void setMediaImage(String photoId, String photoUri, String videoId, String voiceId, String handwritingId) {
        Log.d(TAG, "photoId : " + photoId + ", photoUri : " + photoUri);
        if(photoId.equals("") || photoId.equals("-1")){
            mPhoto.setImageResource(R.drawable.person_add);
        } else {
            isPhotoFileSaved = true;
            mPhoto.setImageURI(Uri.parse(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri));
        }

    }

    private void setCalendar() {
        insertDateButton = (TitleBitmapButton)findViewById(R.id.insert_dateBtn);
        insertDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mDateStr = insertDateButton.getText().toString();
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                try {
                    date = BasicInfo.dateDayNameFormat.parse(mDateStr);
                } catch (ParseException e) {
                    Log.d(TAG, "Exception in parsing date : " + date);
                }

                calendar.setTime(date);


                new DatePickerDialog(getApplicationContext(), dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();

            }
        });

        Date curDate = new Date();
        mCalendar.setTime(curDate);

        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        insertDateButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(year, monthOfYear, dayOfMonth);
            insertDateButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }
    };

    private void setBottomButtons() {
        insertSaveBtn = (TitleBitmapButton)findViewById(R.id.insert_saveBtn);
        insertCancelBtn = (TitleBitmapButton)findViewById(R.id.insert_cancelBtn);

        insertSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isParsed = parseValues();
                if(isParsed){
                    if(mMemoMode.equals(BasicInfo.MODE_INSERT) ){
                        saveInput();
                    } else if(mMemoMode.equals(BasicInfo.KEY_MEMO_MODE) || mMemoMode.equals(BasicInfo.MODE_VIEW)){
                        modifyInput();
                    }

                }
            }
        });

        insertCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void modifyInput() {

        Intent intent = getIntent();

        String photoFilename = insertPhoto();
        int photoId = -1;

        String SQL = null;

        if(photoFilename != null){
            SQL = "SELECT _ID FROM " + MemoDatabase.TABLE_PHOTO + " WHERE URI = '" + photoFilename + "'";
            Log.d(TAG, "SQL : " + SQL);
            if(MultiMemoActivity.mDatabase != null){
                Cursor cursor = MultiMemoActivity.mDatabase.rawQuery(SQL);
                if(cursor.moveToNext()){
                    photoId = cursor.getInt(0);
                }
                cursor.close();

                mMediaPhotoUri = photoFilename;

                SQL = "UPDATE " + MemoDatabase.TABLE_MEMO +
                        "SET " +
                        "ID_PHOTO = '" + photoId + "'" +
                        "WHERE _id = '" + mMemoId + "'";


                if(MultiMemoActivity.mDatabase != null){
                    MultiMemoActivity.mDatabase.rawQuery(SQL);
                }

                mMediaPhotoId = String.valueOf(photoId);
            } else if (isPhotoCanceled && isPhotoFileSaved){
                SQL = "DELETE FROM " + MemoDatabase.TABLE_PHOTO +
                        " WHERE _ID = " + mMediaPhotoId + "'";
                Log.d(TAG, "SQL : " + SQL);
                if(MultiMemoActivity.mDatabase != null){
                    MultiMemoActivity.mDatabase.rawQuery(SQL);
                }

                File photoFile = new File(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri);
                if(photoFile.exists()){
                    photoFile.delete();
                }

                SQL = "update " + MemoDatabase.TABLE_MEMO +
                        " set " +
                        " ID_PHOTO = '" + photoId + "'" +
                        " where _id = '" + mMemoId + "'";

                if (MultiMemoActivity.mDatabase != null) {
                    MultiMemoActivity.mDatabase.rawQuery(SQL);
                }

                mMediaPhotoId = String.valueOf(photoId);

            }
            SQL = "update " + MemoDatabase.TABLE_MEMO +
                    " set " +
                    " INPUT_DATE = DATETIME('" + mDateStr + "'), " +
                    " CONTENT_TEXT = '" + mMemoStr + "'" +
                    " where _id = '" + mMemoId + "'";

            Log.d(TAG, "SQL : " + SQL);
            if (MultiMemoActivity.mDatabase != null) {
                MultiMemoActivity.mDatabase.execSQL(SQL);
            }

            intent.putExtra(BasicInfo.KEY_MEMO_TEXT, mMemoStr);
            intent.putExtra(BasicInfo.KEY_ID_PHOTO, mMediaPhotoId);
            intent.putExtra(BasicInfo.KEY_ID_VIDEO, mMediaVideoId);
            intent.putExtra(BasicInfo.KEY_ID_VOICE, mMediaVoiceId);
            intent.putExtra(BasicInfo.KEY_ID_HANDWRITING, mMediaHandwritingId);
            intent.putExtra(BasicInfo.KEY_URI_PHOTO, mMediaPhotoUri);
            intent.putExtra(BasicInfo.KEY_URI_VIDEO, mMediaVideoUri);
            intent.putExtra(BasicInfo.KEY_URI_VOICE, mMediaVoiceUri);
            intent.putExtra(BasicInfo.KEY_URI_HANDWRITING, mMediaHandwritingUri);

            setResult(RESULT_OK, intent);
            finish();



        }


    }

    private void saveInput() {
        
        String phtoFilename = insertPhoto();
        int photoId = -1;
        
        
    }

    private String insertPhoto() {
        String photoName = null;

        if(isPhotoCaptured){
            try{
                if(mMemoMode != null && mMemoMode.equals(BasicInfo.MODE_MODIFY)) {
                    Log.d(TAG, "previous photo is newly created for modify mode.");

                    String SQL = "DELETE FROM " + MemoDatabase.TABLE_PHOTO + " WHERE _ID = '" + mMemoId + "'";
                    Log.d(TAG, "SQL : " + SQL);
                    if (MultiMemoActivity.mDatabase != null) {
                        MultiMemoActivity.mDatabase.execSQL(SQL);
                    }


                    File previousFile = new File(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri);
                    if (previousFile.exists()) {
                        previousFile.delete();
                    }
                }

                File photoFolder = new File(BasicInfo.FOLDER_PHOTO);

                if (!photoFolder.isDirectory()) {
                    Log.d(TAG, "creating photo folder : " + photoFolder);
                    photoFolder.mkdirs();
                }
                    photoName = createFilename();

                    FileOutputStream outstream = new FileOutputStream(BasicInfo.FOLDER_PHOTO + photoName);

                    resultPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
                    outstream.close();

                    if (photoName != null) {
                        Log.d(TAG, "isCapture : " + isPhotoCaptured);

                        String SQL = "INSERT INTO " + MemoDatabase.TABLE_PHOTO + "(URI) values(" +
                        "'" + photoName + "')";
                        if(MultiMemoActivity.mDatabase != null){
                            MultiMemoActivity.mDatabase.execSQL(SQL);
                        }

                    }

            } catch (Exception e)
            {
                Log.e(TAG, "Exception in copying photo : " + e.toString());
            }
        }

        return photoName;
    }

    private String createFilename() {
        Date curDate = new Date();
        String curDateStr = String.valueOf(curDate.getTime());
        return curDateStr;
    }

    private boolean parseValues() {
        String insertDateStr = insertDateButton.getText().toString();
        try {
            Date insertDate = BasicInfo.dateDayNameFormat.parse(insertDateStr);
            mDateStr = BasicInfo.dateDayFormat.format(insertDate);
        } catch (ParseException e) {
            Log.e(TAG, "Exception in parsing date : " + insertDateStr);
        }

        String memotxt = mMemoEdit.getText().toString();
        mMemoStr = memotxt;

        if(mMemoStr.trim().length() < 1){
            showDialog(BasicInfo.CONFIRM_TEXT_INPUT);
            return false;
        }

        return true;
    }

    private void setMediaLayout() {
    }



    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
