package com.yunjian.ak.kong.client.model.admin.target;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;

/**
 * Created by vaibhav on 14/06/17.
 */
@Data
public class Target {

    @SerializedName("id")
    private String id;
    @SerializedName("target")
    private String target;
    @SerializedName("weight")
    private Integer weight;
    @SerializedName("upstream_id")
    private String upstreamId;
}
