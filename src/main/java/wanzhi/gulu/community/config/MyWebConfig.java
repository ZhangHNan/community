package wanzhi.gulu.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wanzhi.gulu.community.interceptor.LoginInterceptor;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器loginInterceptor处理所有请求，静态资源不拦截
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
