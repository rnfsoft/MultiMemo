package com.rnfsoft.multimemo;

import java.util.Objects;

/**
 * Created by taehee on 7/31/15.
 */
public class MemoListItem {
    private String[] mData;
    private String mId;

    private boolean mSelectable = true;

    public MemoListItem(String mId, String[] mData) {
        this.mId = mId;
        this.mData = mData;

    }

    public MemoListItem(String mId, String memoDate, String memoText,
                        String id_handwriting, String uri_handwriting,
                        String id_photo, String uri_photo,
                        String id_video, String uri_video,
                        String id_voice, String uri_voice){
        this.mId = mId;
        this.mData = new String[10];
        this.mData[0] = memoDate;
        this.mData[1] = memoText;
        this.mData[2] = id_handwriting;
        this.mData[3] = uri_handwriting;
        this.mData[4] = id_photo;
        this.mData[5] = uri_photo;
        this.mData[6] = id_video;
        this.mData[7] = uri_video;
        this.mData[8] = id_voice;
        this.mData[9] = uri_voice;

    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public Object[] getData() {
        return mData;
    }

    public String getData(int index){
        if(mData ==null || index >= mData.length){
            return null;
        }
        return mData[index];
    }






}
