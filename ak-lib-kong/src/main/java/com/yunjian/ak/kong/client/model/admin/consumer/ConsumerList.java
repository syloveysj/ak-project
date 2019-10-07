package com.yunjian.ak.kong.client.model.admin.consumer;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
@Data
public class ConsumerList extends AbstractEntityList {
    Long total;
    String next;
    List<Consumer> data;
}
