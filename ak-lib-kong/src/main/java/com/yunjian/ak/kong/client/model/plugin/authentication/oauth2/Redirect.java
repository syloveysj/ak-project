package com.yunjian.ak.kong.client.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Created by vaibhav on 15/06/17.
 */
@Data
public class Redirect {
    @SerializedName("redirect_uri")
    String redirectUri;
}
