package com.nchu.lecturerplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {//视图控制器
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {//资源映射路径
        registry.addResourceHandler("/course_images/**").addResourceLocations("file:D:/LecturerPlatform/courses/images/");
        registry.addResourceHandler("/course_videos/**").addResourceLocations("file:D:/LecturerPlatform/courses/videos/");
        registry.addResourceHandler("/docs/**").addResourceLocations("file:D:/LecturerPlatform/courses/docs/");
        registry.addResourceHandler("/avatars/**").addResourceLocations("file:D:/LecturerPlatform/avatars/");
        registry.addResourceHandler("/bills/**").addResourceLocations("file:D:/LecturerPlatform/bills/");
    }

}
