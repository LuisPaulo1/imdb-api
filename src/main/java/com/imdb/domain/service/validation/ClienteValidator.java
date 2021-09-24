package com.imdb.domain.service.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.imdb.api.exception.FieldMessage;
import com.imdb.api.model.input.ClienteInput;
import com.imdb.domain.model.Cliente;
import com.imdb.domain.repository.ClienteRepository;

public class ClienteValidator implements ConstraintValidator<ClienteValid, ClienteInput> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repository;
	
	@Override
	public void initialize(ClienteValid ann) {
	}

	@Override
	public boolean isValid(ClienteInput dto, ConstraintValidatorContext context) {
		
		Cliente user = repository.findByEmail(dto.getEmail());
		List<FieldMessage> list = new ArrayList<>();
		
		if(request.getMethod().equals("PUT")) {
			
			@SuppressWarnings("unchecked")
			var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			long userId = Long.parseLong(uriVars.get("id"));
			
			if (user != null && userId != user.getId()) 
				list.add(new FieldMessage("email", "Email já existe"));	
			
		}else {			
			if (user != null) 
				list.add(new FieldMessage("email", "Email já existe"));			
		}		
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
