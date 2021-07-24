package com.imdb.core.openapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.classmate.TypeResolver;
import com.imdb.api.exception.Problem;
import com.imdb.api.model.ClienteResumoModel;
import com.imdb.api.model.FilmeResumoModel;
import com.imdb.api.openapi.model.ClientesModelOpenApi;
import com.imdb.api.openapi.model.FilmesModelOpenApi;
import com.imdb.api.openapi.model.PageableModelOpenApi;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SpringFoxConfig implements WebMvcConfigurer {
	
	@Bean
	public Docket apiDocket() {
		
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
			.select()
				.apis(RequestHandlerSelectors.basePackage("com.imdb.api"))
				.build()
				.apiInfo(apiInfo())
				.tags(tags()[0], tags())		
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class))
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
				.alternateTypeRules(newRule(Page.class, ClienteResumoModel.class, ClientesModelOpenApi.class))								
				.alternateTypeRules(newRule(Page.class, FilmeResumoModel.class, FilmesModelOpenApi.class))
				.securitySchemes(List.of(authenticationScheme()))
				.securityContexts(List.of(securityContext()));
	}	
	
	private <T, M, K> AlternateTypeRule newRule(Class<T> returnType, Class<M> modelObject, Class<K> modelObjectOpenApi) {
		var typeResolver = new TypeResolver();
		return AlternateTypeRules.newRule(
				typeResolver.resolve(ResponseEntity.class, typeResolver.resolve(returnType, modelObject)),
				typeResolver.resolve(modelObjectOpenApi),
				Ordered.HIGHEST_PRECEDENCE);		
	}
	
	private HttpAuthenticationScheme authenticationScheme() {
		return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("Authorization").build();
	}
	
	private SecurityContext securityContext() {		
		return SecurityContext.builder()
				.securityReferences(securityReference())
				.build();
	}
	
	 private List<SecurityReference> securityReference() {
	        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return List.of(new SecurityReference("Authorization", authorizationScopes));
	 }
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("IMDb API")
				.description("API aberta para a IMDb")
				.version("1.0")
				.contact(new Contact("IMDb", "https://www.imdb.com", "contato@imdb.com"))
				.build();
	}
	
	private Tag[] tags() {
		return new Tag[] {
				new Tag("Filmes", "Gerencia os filmes"),
				new Tag("Clientes", "Gerencia os clientes"),				
				new Tag("Atores", "Gerencia os atores")				
		};
	}
	
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build()
		);			
	}	
	
	private List<Response> globalPostPutResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Requisição inválida (erro do cliente)")
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build(),
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),				
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Requisição recusada porque o corpo está em um formato não suportado")
				.build(),		
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.build()				
		);			
	}			
	
	private List<Response> globalDeleteResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Requisição inválida (erro do cliente)")
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build()
		);			
	}	
	
	private Consumer<RepresentationBuilder> builderModelProblema() {
		return r -> r.model(m -> m.name("Problema")
				.referenceModel(
					ref -> ref.key(
							k -> k.qualifiedModelName(q -> q.name("Problema").namespace("com.imdb.exception")
						))));
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("index.html")
         .addResourceLocations("classpath:/META-INF/resources/");
	}
	
}
