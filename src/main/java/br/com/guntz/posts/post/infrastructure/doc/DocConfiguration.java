package br.com.guntz.posts.post.infrastructure.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Guntz Post Service - Microsserviço")
                        .description("""
                                Guntz Post é um microsserviço de criação de posts via API REST construído com Spring Boot.\n
                                Esse microsserviço exibe posts com recurso de páginação, além de pesquisa individual por Id (UUID),\n
                                Válida e armazena apenas posts que cumpram com todos requisitos inicias como as propriedades: title, body e author serem obrigatórias).\n
                                Em <b>segundo plano</b> envia os posts para uma fila do Rabbitmq <b>(text-processor-service.post-processing.v1.q)</b>\n
                                Esse microsserviço faz comunicação asíncrona com outro microsserviço <b>(Guntz Text Processor Service)</b> via RabbitMQ.\n
                                Que por sua vez é responsável pelo processamento dos posts que foram enviados anteriormente pelo outro microsserviço(Guntz Post Service).\n
                                Ele vai consumir a fila (text-processor-service.post-processing.v1.q), e na sequência iniciará a contagem de palavras no texto e o cálculo de um valor estimado com base na quantidade de palavras.\n
                                Uma vez processado, o resultado será enviado para a fila <b>(post-service.post-processing-result.v1.q)</b> que por sua vez será consumido pelo microsserviço <b>Guntz Post Service</b>.
                                """
                        )
                        .contact(new Contact()
                                .name("Guntz")
                                .email("rricardoguntzell@gmail.com"))
                        .license(new License()
                                .name("Guntz - Github")
                                .url("https://github.com/ricardoguntzell/guntz-guntzposts-post-service.git")));
    }
}
