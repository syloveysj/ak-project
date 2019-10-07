package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.plugin.EnabledPlugins;

public interface PluginRepoService {

    EnabledPlugins retrieveEnabledPlugins();

    Object retrievePluginSchema(String pluginName);
}
