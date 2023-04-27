package pl.twojekursy.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.twojekursy.tracking.TrackingFilter;

@Configuration
public class TrackingConfig {
    @Bean
    FilterRegistrationBean<TrackingFilter> registerTrackingFilter(){
        FilterRegistrationBean<TrackingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrackingFilter());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
