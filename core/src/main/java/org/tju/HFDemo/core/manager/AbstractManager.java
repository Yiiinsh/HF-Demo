package org.tju.HFDemo.core.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tju.HFDemo.common.config.Config;

/**
 * Created by shaohan.yin on 30/04/2017.
 */
public abstract class AbstractManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Config config = Config.DEFAULT;

    protected String getConfig(String key) {
        return config.getString(key);
    }

    protected String getConfig(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

}
