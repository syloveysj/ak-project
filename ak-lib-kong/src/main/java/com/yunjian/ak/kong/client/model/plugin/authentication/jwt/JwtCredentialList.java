package com.yunjian.ak.kong.client.model.plugin.authentication.jwt;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 16/06/17.
 */
@Data
public class JwtCredentialList extends AbstractEntityList {
    Long total;
    List<JwtCredential> data;
}
