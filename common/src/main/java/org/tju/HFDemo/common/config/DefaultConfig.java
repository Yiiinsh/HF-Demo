package org.tju.HFDemo.common.config;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shaohan.yin on 17/04/2017.
 */
public class DefaultConfig extends CompositeConfiguration implements Config {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultConfig() {
        super();

        try {
            this.addConfiguration(getDefaultPropertiesConfiguration());
        } catch (ConfigurationException e) {
            logger.error("Cannot load from default file.", e);
        }
    }

    private Configuration getDefaultPropertiesConfiguration() throws ConfigurationException {
        Parameters params = new Parameters();
        String defaultConfigurationName = "configurations.properties";

        return (new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.properties()
                        .setFileName(defaultConfigurationName)
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))))
                .getConfiguration();
    }

}
