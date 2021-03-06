package com.imdb.api.exception;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.imdb.domain.exception.AuthorizationException;
import com.imdb.domain.exception.EntidadeNaoEncontradaException;
import com.imdb.domain.exception.NegocioException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {	
	
	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = 
			"Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";
	
	@Autowired
	private MessageSource messageSource;
	
	//Erros no body/json  
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ex.getRootCause();	
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if(rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}		
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisi????o est?? inv??lido. Verifique erro de sintaxe.";		
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que ?? de um tipo inv??lido. Corrija e informe um valor compat??vel com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' n??o existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.", path);

		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {		
		return ResponseEntity.status(status).headers(headers).build();
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(ex instanceof MethodArgumentTypeMismatchException)
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);	
				
		return super.handleTypeMismatch(ex, headers, status, request);
	}	
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		
		String detail = String.format("O par??metro de URL '%s' recebeu o valor '%s', que ?? de um tipo inv??lido. "
				+"Corrija e informe um valor compat??vel com o tipo %s.", ex.getName(), ex.getValue(), 
				ex.getRequiredType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();		
		
		return handleExceptionInternal(ex, problem, headers, status, request);		
	}
	
	@Override 
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, 
			  HttpStatus status, WebRequest request) { 
		  
	  ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO; 
	  
	  String detail = String.format("O recurso %s que voc?? tentou acessar, ?? inexistente.", ex.getRequestURL()); 
	  
	  Problem problem = createProblemBuilder(status, problemType, detail)
			  .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
			  .build();
	  
	  return handleExceptionInternal(ex, problem,headers, status, request);
	}
	
	//Requisi????o sem informar o @RequestParam do endpoint 
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO; 
		  
		String detail = String.format("O par??metro de URL '%s' ?? obrigat??rio. "
				+ "Informe o par??metro e o seu valor antes de solicitar uma nova requisi????o.", 
				ex.getParameterName());
						  
		Problem problem = createProblemBuilder(status, problemType, detail)
				  .userMessage(detail)
				  .build();		
		
		return handleExceptionInternal(ex, problem,headers, status, request);
	}
	
	
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<?> handleParametroIncorreto(InvalidDataAccessApiUsageException ex, WebRequest request) {		
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		
		String detail = String.format("Par??metro inv??lido. Corriga e informe um par??metro v??lido.");			
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();	
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}	
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<?> handlePropriedadeInvalida(PropertyReferenceException ex, WebRequest request) {		
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		
		String detail = String.format("O valor do par??metro de URL '%s' ?? uma propriedade inexistente para o tipo %s. "
				+ "Corriga e informe uma propriedade v??lida.", ex.getPropertyName(), ex.getType());			
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();	
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}	
	
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<?> handleArgumentoInvalido(NumberFormatException ex, WebRequest request) {		
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		String detail = ex.getMessage();		
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleValidation(ConstraintViolationException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getLocalizedMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<?> handleAuthorization(AuthorizationException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.FORBIDDEN;
		ProblemType problemType = ProblemType.ACESSO_NEGADO;
		String detail = ex.getLocalizedMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAcessDenied(AccessDeniedException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.FORBIDDEN;
		ProblemType problemType = ProblemType.ACESSO_NEGADO;
		String detail = "Este tipo de cliente n??o tem permiss??o para acessar este recurso.";		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {		
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();		
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}	
		
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleErroDeSistema(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;
				
		log.error(ex.getMessage(), ex);
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail) 
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}
		
	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
		HttpStatus status, WebRequest request) {
	        
	    ProblemType problemType = ProblemType.DADOS_INVALIDOS;
	    String detail = "Um ou mais campos est??o inv??lidos. Fa??a o preenchimento correto e tente novamente.";
	    
	    List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
	            .map(objectError -> {
	                String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
	                
	                String name = objectError.getObjectName();
	                
	                if (objectError instanceof FieldError) {
	                    name = ((FieldError) objectError).getField();
	                }
	                
	                return Problem.Object.builder()
	                    .name(name)
	                    .userMessage(message)
	                    .build();
	            })
	            .collect(Collectors.toList());
	    
	    Problem problem = createProblemBuilder(status, problemType, detail)
	        .userMessage(detail)
	        .objects(problemObjects)
	        .build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	//trata exce????es do bean validation
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, 
			WebRequest request) {
	    return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	//trata par??metros de URI inv??lido
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = Problem.builder()
					.timestamp(OffsetDateTime.now())
					.title(status.getReasonPhrase())
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.build();
		} else if (body instanceof String) {
			body = Problem.builder()
					.timestamp(OffsetDateTime.now())
					.title((String) body)
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
			ProblemType problemType, String detail) {
		
		return Problem.builder()
			.timestamp(OffsetDateTime.now())
			.status(status.value())
			.type(problemType.getUri())
			.title(problemType.getTitle())
			.detail(detail);
	}	
	
	private String joinPath(List<Reference> references) {
	    return references.stream()
	        .map(ref -> ref.getFieldName())
	        .collect(Collectors.joining("."));
	}
}
