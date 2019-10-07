package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.consumer.Consumer;
import com.yunjian.ak.kong.client.model.admin.consumer.ConsumerList;

/**
 * Created by vaibhav on 13/06/17.
 */
public interface ConsumerService {

    Consumer createConsumer(Consumer request);

    Consumer getConsumer(String usernameOrId);

    Consumer updateConsumer(String usernameOrId, Consumer request);

    @Deprecated
    Consumer createOrUpdateConsumer(Consumer request);

    void deleteConsumer(String usernameOrId);

    ConsumerList listConsumers(String id, String customId, String username, Long size, String offset);
}
