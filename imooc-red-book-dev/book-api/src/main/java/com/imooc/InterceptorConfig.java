package com.imooc;

/**
 * @author 包建丰
 * @date 2021/11/23 18 :49
 * @description
 **/

import com.imooc.intercepter.PassportInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器的配置类，用于注册和生效拦截器
 * @author 包建丰
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 在springboot容器中放入拦截器
     *
     * @return
     */
    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }

    /**
     * 注册拦截器，并且拦截指定的路由，否则不生效
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/getSMSCode");

    }
}

