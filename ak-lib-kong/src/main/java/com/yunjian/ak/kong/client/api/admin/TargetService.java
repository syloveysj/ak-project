package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.target.Target;
import com.yunjian.ak.kong.client.model.admin.target.TargetList;

/**
 * Created by vaibhav on 13/06/17.
 */
@Deprecated
public interface TargetService {
    Target createTarget(String upstreamNameOrId, Target request);

    Target deleteTarget(String upstreamNameOrId, String target);

    TargetList listTargets(String upstreamNameOrId, String id, Integer weight, String target, Long size, String offset);

    TargetList listActiveTargets(String upstreamNameOrId);
}
