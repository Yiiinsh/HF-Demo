package config;

import org.apache.commons.configuration2.ImmutableConfiguration;

/**
 * Created by shaohan.yin on 17/04/2017.
 */
public interface Config extends ImmutableConfiguration {
    Config DEFAULT = new DefaultConfig();
}
