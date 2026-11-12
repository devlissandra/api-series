package br.com.senac.apiseries.config;

import jakarta.servlet.http.HttpServletRequest;

import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class SwaggerCustomConfig {

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(
            SwaggerUiConfigProperties swaggerUiConfigProperties,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerWelcomeCommon swaggerWelcomeCommon,
            ObjectMapperProvider objectMapperProvider) {

        return new SwaggerIndexPageTransformer(
                swaggerUiConfigProperties,
                swaggerUiOAuthProperties,
                swaggerWelcomeCommon,
                objectMapperProvider) {

            @Override
            public Resource transform(
                    HttpServletRequest request,
                    Resource resource,
                    ResourceTransformerChain transformer)
                    throws IOException {

                if (resource.getFilename() != null
                        && resource.getFilename().equals("index.html")) {

                    String html;

                    try (InputStream inputStream = resource.getInputStream()) {
                        html = new String(
                                inputStream.readAllBytes(),
                                StandardCharsets.UTF_8
                        );
                    }

                    ClassPathResource cssResource =
                            new ClassPathResource("static/swagger-custom.css");

                    String css;

                    try (InputStream inputStream = cssResource.getInputStream()) {
                        css = new String(
                                inputStream.readAllBytes(),
                                StandardCharsets.UTF_8
                        );
                    }

                    String styleTag =
                            "<style>\n"
                                    + css
                                    + "\n</style>";

                    html = html.replace(
                            "</head>",
                            styleTag + "\n</head>"
                    );

                    return new TransformedResource(
                            resource,
                            html.getBytes(StandardCharsets.UTF_8)
                    );
                }

                return super.transform(request, resource, transformer);
            }
        };
    }
}