package com.dcool.springchatexample;


import java.io.IOException;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class ApplicationWebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowCredentials(false)
                .maxAge(30000);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList(CorsConfiguration.ALL));
        configuration.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        configuration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(30000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//
//   @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//       registry.addResourceHandler("/**")
//               .addResourceLocations("classpath:/META-INF/resources/")
//               .setCachePeriod(20)
//               .resourceChain(true)
//               .addResolver(new PathResourceResolver() {
//                   @Override
//                   protected Resource getResource(String resourcePath, Resource location) throws IOException {
//                       Resource r = location.createRelative(resourcePath);
//                       return r.exists() && r.isReadable() ? r : new ClassPathResource("/META-INF/resources/index.html");
//                   }
//               });
//
//        registry.addResourceHandler("/docs")
//                .addResourceLocations("classpath:/META-INF/docs")
//                .setCachePeriod(20)
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver() {
//                    @Override
//                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
//                        Resource r = location.createRelative(resourcePath);
//                        return r.exists() && r.isReadable() ? r : new ClassPathResource("/META-INF/docs/index.html");
//                    }
//                });
//    }



}