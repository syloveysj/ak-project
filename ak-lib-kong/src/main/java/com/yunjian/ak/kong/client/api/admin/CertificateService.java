package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.certificate.Certificate;
import com.yunjian.ak.kong.client.model.admin.certificate.CertificateList;

/**
 * Created by vaibhav on 13/06/17.
 */
@Deprecated
public interface CertificateService {
    Certificate createCertificate(Certificate request);

    Certificate getCertificate(String sniOrId);

    Certificate updateCertificate(String sniOrId, Certificate request);

    Certificate createOrUpdateCertificate(Certificate request);

    Certificate deleteCertificate(String sniOrId);

    CertificateList listCertificates();
}
