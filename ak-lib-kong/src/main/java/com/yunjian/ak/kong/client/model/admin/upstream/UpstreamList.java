package com.yunjian.ak.kong.client.model.admin.upstream;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
@Data
public class UpstreamList extends AbstractEntityList {
    Long total;
    String next;
    List<Upstream> data;
}
