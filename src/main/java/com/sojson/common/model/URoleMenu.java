package com.sojson.common.model;

import java.io.Serializable;

public class URoleMenu implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6407435495967513910L;

	private Long rId;

    private Long mId;

    public Long getrId() {
        return rId;
    }

    public void setrId(Long rId) {
        this.rId = rId;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }
}