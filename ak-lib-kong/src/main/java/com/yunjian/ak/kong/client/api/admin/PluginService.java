package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.plugin.Plugin;
import com.yunjian.ak.kong.client.model.admin.plugin.PluginList;

/**
 * Created by vaibhav on 13/06/17.
 * <p>
 * You can add a plugin in four different ways:
 * For every API and Consumer. Don't set api_id and consumer_id.
 * For every API and a specific Consumer. Only set consumer_id.
 * For every Consumer and a specific API. Only set api_id.
 * For a specific Consumer and API. Set both api_id and consumer_id.
 * Note that not all plugins allow to specify consumer_id. Check the plugin documentation.
 */
public interface PluginService {


    Plugin addPlugin(Plugin request);

    Plugin getPlugin(String nameOrId);

    Plugin updatePlugin(String nameOrId, Plugin request);

    @Deprecated
    Plugin createOrUpdatePlugin(Plugin request);

    void deletePlugin(String nameOrId);

    PluginList listPlugins(String id, String apiId, String consumerId, String name, Long size, String offset);
}
