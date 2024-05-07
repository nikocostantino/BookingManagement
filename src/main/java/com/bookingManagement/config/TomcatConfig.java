package com.bookingManagement.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;

@Configuration
@ConditionalOnProperty(name = "external.enabled")
@Slf4j
public class TomcatConfig {

    private static final String CHILD_TOM = "webapps";

    @Value("${server.tomcat.skip-jars}")
    private String skipJars;

    @Bean
    @ConditionalOnProperty(name = "external.war.file")
    public TomcatServletWebServerFactory servletContainerFactory(@Value("${external.war.file}") String path,
                                                                 @Value("${external.war.context}") String contextPath) {
        return new TomcatServletWebServerFactory() {

            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {

                var created = new File(tomcat.getServer().getCatalinaBase(), CHILD_TOM).mkdirs();

                log.debug("TomcatWebServer -> CREATED {}: {}", CHILD_TOM, created);

                var paths = Arrays.asList(path.split(","));
                var contextPaths = Arrays.asList(contextPath.split(","));

                if (paths.size() == contextPaths.size()) {
                    paths.forEach(pathElement -> {
                        var idx = paths.indexOf(pathElement);
                        var context = tomcat.addWebapp(contextPaths.get(idx), pathElement);
                        // to avoid duplicate dependencies
                        context.setParentClassLoader(getClass().getClassLoader());

                        if (!skipJars.isEmpty()) {
                            var scanFilter = new StandardJarScanFilter();
                            scanFilter.setDefaultTldScan(false);
                            scanFilter.setTldSkip(skipJars);
                            scanFilter.setDefaultPluggabilityScan(false);
                            scanFilter.setPluggabilityScan(skipJars);
                            var jarScanner = context.getJarScanner();
                            jarScanner.setJarScanFilter(scanFilter);
                            log.debug("ADDED {} TO CONTEXT {} SKIPPING {}", pathElement, contextPaths.get(idx), skipJars);
                        }
                    });
                } else {
                    log.debug("ERROR ADDING {} ON CONTEXT(S) {}", paths, contextPaths);
                }

                return super.getTomcatWebServer(tomcat);
            }

            @Override
            protected void postProcessContext(Context context) {
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }

        };
    }
}

