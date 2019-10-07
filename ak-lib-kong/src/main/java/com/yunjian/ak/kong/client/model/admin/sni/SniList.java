package com.yunjian.ak.kong.client.model.admin.sni;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
@Data
public class SniList extends AbstractEntityList {
    Long total;
    List<Sni> data;
}
