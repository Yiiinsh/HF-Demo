package org.tju.HFDemo.core.manager;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tju.HFDemo.core.config.HFDConfig;

/**
 * Created by shaohan.yin on 30/04/2017.
 */
public abstract class AbstractManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected HFDConfig config = HFDConfig.DEFAULT;
    protected Gson gson = new Gson();

    protected boolean withinAdmin() {
        // TODO: setup simple admin for all query
        return true;
    }
}
