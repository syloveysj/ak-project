package com.yunjian.ak.kong.client.impl.service.plugin.security;

import com.yunjian.ak.kong.client.api.plugin.security.AclService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.internal.plugin.security.RetrofitAclService;
import com.yunjian.ak.kong.client.model.plugin.security.acl.Acl;
import com.yunjian.ak.kong.client.model.plugin.security.acl.AclList;

import java.io.IOException;

/**
 * Created by vaibhav on 18/06/17.
 * <p>
 * Updated by fanhua on 2017-08-07.
 * <p>
 * Upated by dvilela on 22/10/17.
 */
public class AclServiceImpl implements AclService {

    private RetrofitAclService retrofitAclService;

    public AclServiceImpl(RetrofitAclService retrofitAclService) {
        this.retrofitAclService = retrofitAclService;
    }

    @Override
    public void associateConsumer(String usernameOrId, String group) {
        try {
            retrofitAclService.associateConsumer(usernameOrId, new Acl(group)).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public AclList listAcls(String usernameOrId, Long size, String offset) {
        try {
            return retrofitAclService.listAcls(usernameOrId, size, offset).execute().body();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
