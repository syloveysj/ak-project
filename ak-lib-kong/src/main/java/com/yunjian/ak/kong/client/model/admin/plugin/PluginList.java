package com.yunjian.ak.kong.client.model.admin.plugin;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
@Data
public class PluginList extends AbstractEntityList {
    Long total;
    String next;
    List<Plugin> data;
}
