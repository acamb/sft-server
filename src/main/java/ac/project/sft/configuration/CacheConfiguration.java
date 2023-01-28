package ac.project.sft.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class CacheConfiguration {

    @Bean
    CacheManager getCacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager("caches");
        manager.registerCustomCache("price",Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofDays(1)).build());
        return manager;
    }
}
