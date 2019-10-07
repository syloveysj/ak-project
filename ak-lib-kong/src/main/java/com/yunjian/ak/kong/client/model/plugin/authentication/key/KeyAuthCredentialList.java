package com.yunjian.ak.kong.client.model.plugin.authentication.key;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by dvilela on 17/10/2017.
 */
@Data
public class KeyAuthCredentialList extends AbstractEntityList {
    Long total;
    List<KeyAuthCredential> data;
}
