package com.yunjian.ak.kong.client.model.plugin.trafficcontrol.requestsizelimiting;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Created by vaibhav on 18/06/17.
 */
@Data
public class RequestSizeLimitingConfig {
    @SerializedName("allowed_payload_size")
    Integer allowedPayloadSize;
}
