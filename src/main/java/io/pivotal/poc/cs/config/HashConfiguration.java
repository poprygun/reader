package io.pivotal.poc.cs.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import io.pivotal.poc.cs.ReaderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashConfiguration {
    private static final int RECEICED_MESSAGES_TRACK_TTL_SECS = 60 * 60;

//    @Bean
//    public Config config() {
//        Config config = new Config();
//        config.addMapConfig(
//                // Set up TTL for the Map tracking received Messages IDs
//                new MapConfig()
//                        .setName(ReaderServiceImpl.ACCEPTED_MESSAGES_TRACKING_MAP_NAME)
//                        .setEvictionPolicy(EvictionPolicy.LRU)
//                        .setTimeToLiveSeconds(RECEICED_MESSAGES_TRACK_TTL_SECS));
//        config.getNetworkConfig()
//                .setPublicAddress("10.254.0.26")
////                .setPublicAddress("192.168.11.1")
//                .setPort(5701);
//        return config;
//    }

}
