package com.yunjian.ak.kong.client.model.common;

import com.yunjian.ak.kong.client.utils.UrlUtil;

/**
 * Created by vaibhav on 13/06/17.
 */
public abstract class AbstractEntityList {

    public String getNext() {
        return null;
    }

    public String getOffset() {
        if (getNext() == null) {
            return null;
        } else {
            return UrlUtil.splitQueryString(getNext()).get("offset");
        }
    }

}
