package com.rnfsoft.multimemo;

import java.text.SimpleDateFormat;

/**
 * Created by taehee on 7/31/15.
 */
public class BasicInfo {

    public static String ExternalPath = "/sdcard/";

    public static boolean ExternalChecked = false;

    public static  String FOLDER_PHOTO = "MultimediaMemo/photo/";

    public static String FOLDER_VIDEO 		= "MultimediaMemo/video/";

    public static String FOLDER_VOICE 		= "MultimediaMemo/voice/";

    public static String FOLDER_HANDWRITING 	= "MultimediaMemo/handwriting/";


    public static String DATABASE_NAME = "MultimediaMemo/memo.db";






    public static final String KEY_MEMO_MODE = "MEMO_MODE";
    public static final String KEY_MEMO_TEXT = "MEMO_TEXT";
    public static final String KEY_MEMO_ID = "MEMO_ID";
    public static final String KEY_MEMO_DATE = "MEMO_DATE";
    public static final String KEY_ID_PHOTO = "ID_PHOTO";
    public static final String KEY_URI_PHOTO = "URI_PHOTO";
    public static final String KEY_ID_VIDEO = "ID_VIDEO";
    public static final String KEY_URI_VIDEO = "URI_VIDEO";
    public static final String KEY_ID_VOICE = "ID_VOICE";
    public static final String KEY_URI_VOICE = "URI_VOICE";
    public static final String KEY_ID_HANDWRITING = "ID_HANDWRITING";
    public static final String KEY_URI_HANDWRITING = "URI_HANDWRITING";


    public static final String MODE_INSERT = "MODE_INSERT";
    public static final String MODE_MODIFY = "MODE_MODIFY";
    public static final String MODE_VIEW = "MODE_VIEW";

    public static final int REQ_INSERT_ACTIVITY = 1002;
    public static final int REQ_VIEW_ACTIVITY = 1001;

    public static final int CONTENT_PHOTO = 2001;
    public static final int CONTENT_PHOTO_EX = 2005;
    public static SimpleDateFormat dateDayNameFormat = new SimpleDateFormat("MMM, dd yyyy");
    public static SimpleDateFormat dateDayFormat = new SimpleDateFormat("MM-dd-yyyy");

    public static final int CONFIRM_TEXT_INPUT = 3002;


}
