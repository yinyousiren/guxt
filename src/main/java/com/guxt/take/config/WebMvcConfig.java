package com.guxt.take.config;

import com.guxt.take.common.JacksonObjectMapper;
import com.guxt.take.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //@Autowired
    //private LoginInterceptor loginInterceptor;
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(getLoginInterceptor());
        registration.addPathPatterns("/**");
        //registration.excludePathPatterns(
        //        "/backend/page/login/login.html",
        //        "/**/*.js",
        //        "/**/*.css",
        //        "backend/images/**"
        //);
        registration.excludePathPatterns(getLoginInterceptor().getUrls());
    }

    /**
     * 扩展MVC的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());


        //将上面的消息转换器对象追加到MVC框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
