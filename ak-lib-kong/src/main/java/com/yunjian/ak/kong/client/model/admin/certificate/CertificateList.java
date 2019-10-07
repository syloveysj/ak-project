package com.yunjian.ak.kong.client.model.admin.certificate;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
@Data
public class CertificateList extends AbstractEntityList {
    Long total;
    List<Certificate> data;
}
