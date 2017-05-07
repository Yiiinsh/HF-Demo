package org.tju.HFDemo.web.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tju.HFDemo.core.manager.ca.CAManager;
import org.tju.HFDemo.core.manager.ca.DefaultCAManager;
import org.tju.HFDemo.core.manager.hf.DefaultHFManager;
import org.tju.HFDemo.core.manager.hf.HFManager;
import org.tju.HFDemo.web.manager.TokenManager;

/**
 * Created by shaohan.yin on 05/05/2017.
 */
@Configuration
public class SpringContextConfig {
    @Bean
    public HFManager getHFManager() {
        return new DefaultHFManager();
    }

    @Bean
    public CAManager getCAManager() {
        return new DefaultCAManager();
    }

    @Bean
    public TokenManager getTokenManager() {
        return new TokenManager();
    }
}
