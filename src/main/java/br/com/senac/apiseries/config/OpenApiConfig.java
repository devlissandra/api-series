
package br.com.senac.apiseries.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Séries API")
                        .version("1.0.0")
                        .description("""
                                API para gerenciamento e organização de séries asiáticas,
                                permitindo cadastrar séries, categorias, temporadas,
                                usuários, perfis e listas de acompanhamento.

                                Fluxo sugerido: cadastre um usuário, crie seu perfil,
                                cadastre séries e categorias e organize seus conteúdos
                                em listas de acompanhamento.
                                """)
                        .termsOfService(
                                "https://github.com/SEU-USUARIO/series-api/blob/main/TERMOS.md"
                        )
                        .contact(new Contact()
                                .name("Lissandra Santos")
                                .email("lissandraforreal@gmail.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/license/mit/")
                        )
                );
    }
}
