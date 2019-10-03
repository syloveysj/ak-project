package com.yunjian.ak.id;

import com.yunjian.ak.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:14
 * @Version 1.0
 */
public class IDGenerator {
    public static IDGenerator INSTANCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(IDGenerator.class);
    private IdWorker worker;

    public IDGenerator(long appid) {
        this.worker = new IdWorker(appid);
        INSTANCE = this;
        LOGGER.info("ID生成器初始化完成，应用id[" + appid + "]，请确保该id在集群内唯一");
    }

    public String createUUID() {
        return UUIDUtil.createUUID();
    }

    public long createLong() {
        if (this.worker == null) {
            throw new IDGenneratorNotInitException();
        } else {
            return this.worker.nextId();
        }
    }
}
