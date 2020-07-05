package com.light.web.aspect;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.light.exception.AuthenticationException;
import com.light.exception.BusinessException;
import com.light.exception.NotFoundException;
import com.light.web.aspect.RestResponseExceptionHandler.HandlerCondition;
import com.light.web.exception.RestResponseException;
import com.light.web.response.ResponseError;
import com.light.web.response.RestResponse;

import cz.jirutka.rsql.parser.RSQLParserException;

@Conditional(HandlerCondition.class)
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestResponseExceptionHandler.class);

	private static final HttpStatus defaultBusinessErrorStatus = HttpStatus.BAD_REQUEST;
	
	@Autowired
	protected MessageSource messageSource;
	
	/**
	 * 框架层面拦截直接抛出的异常，属于API错误使用导致的异常分类，根据HttpStatus的标准返回错误结果<br>
	 * HttpRequestMethodNotSupportedException,<br>
	 * HttpMediaTypeNotSupportedException,<br>
	 * HttpMediaTypeNotAcceptableException,<br>
	 * MissingPathVariableException,<br>
	 * MissingServletRequestParameterException,<br>
	 * ServletRequestBindingException,<br>
	 * ConversionNotSupportedException,<br>
	 * TypeMismatchException,<br>
	 * HttpMessageNotReadableException,<br>
	 * HttpMessageNotWritableException,<br>
	 * MethodArgumentNotValidException,<br>
	 * MissingServletRequestPartException,<br>
	 * BindException,<br>
	 * NoHandlerFoundException,<br>
	 * AsyncRequestTimeoutException<br>
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, Object body,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final String method = ((ServletWebRequest) request).getHttpMethod().toString();
		final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();

		String info = String.format("status code %s, reason %s, [%s] %s", status.value(), status.getReasonPhrase(), method, url);
		logger.error(info, ex);

		if (body == null) {
			body = RestResponse.failure(ResponseError.of(status.getReasonPhrase()).code(String.valueOf(status.value())));
		}
		return new ResponseEntity<>(body, prepareHeaders(headers), status);
	}
	

	/**
	 * 业务自定义异常处理 (BusinessException)
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> exceptionHandler(final BusinessException ex, final HttpServletRequest request) {
		printLog(ex.getCode(), ex, request, Level.INFO);
		final String message = this.getLocalMessage(ex.getCode(), ex.getMessage());
		final RestResponse<Void> result = RestResponse.failure(ResponseError.of(message).code(ex.getCode()));
		return new ResponseEntity<>(result, prepareHeaders(new HttpHeaders()), defaultBusinessErrorStatus);
	}
	
	/*
	 * 资源不存在异常处理 (NotFoundException)
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> exceptionHandler(final NotFoundException ex, final HttpServletRequest request) {
		printLog(ex.getCode(), ex, request, Level.INFO);
		final String message = this.getLocalMessage(ex.getCode(), ex.getMessage());
		final RestResponse<Void> result = RestResponse.failure(ResponseError.of(message).code(ex.getCode()));
		return new ResponseEntity<>(result, prepareHeaders(new HttpHeaders()), HttpStatus.NOT_FOUND);
	}

	/*
	 * 身份认证异常处理 (AuthenticationException)
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> exceptionHandler(final AuthenticationException ex, final HttpServletRequest request) {
		printLog(ex.getCode(), ex, request, Level.INFO);
		final String message = this.getLocalMessage(ex.getCode(), ex.getMessage());
		final RestResponse<Void> result = RestResponse.failure(ResponseError.of(message).code(ex.getCode()));
		return new ResponseEntity<>(result, prepareHeaders(new HttpHeaders()), HttpStatus.UNAUTHORIZED);
	}

	/*
	 * Rest请求结果异常
	 */
	@ExceptionHandler(RestResponseException.class)
	public ResponseEntity<Object> exceptionHandler(final RestResponseException ex, final HttpServletRequest request) {
		printLog(ex.getBody().getError().getCode(), ex, request, Level.INFO);
		return new ResponseEntity<>(ex.getBody(), prepareHeaders(new HttpHeaders()), ex.getStatus());
	}
	
	/*
	 * RSQL异常
	 */
	@ExceptionHandler(RSQLParserException.class)
	public ResponseEntity<Object> rsqlParserExceptionHandler(final RSQLParserException ex, final HttpServletRequest request) {
		printLog("400", ex, request, Level.INFO);
		return new ResponseEntity<>(RestResponse.failure(ResponseError.of("请求参数的RSQL语法错误").code("400")), prepareHeaders(new HttpHeaders()), HttpStatus.BAD_REQUEST);
	}

	/**
	 * RestTemplate 方法调用异常处理 (RestClientException)
	 */
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<Object> exceptionHandler(final RestClientException ex, final HttpServletRequest request) {

		if (ex instanceof HttpStatusCodeException) {
			final HttpStatus status = ((HttpStatusCodeException) ex).getStatusCode();
			final String responseBody = ((HttpStatusCodeException) ex).getResponseBodyAsString();
			printLog(String.valueOf(status.value()), ex, request, Level.ERROR);
			return new ResponseEntity<>(responseBody, prepareHeaders(new HttpHeaders()), status);
		} else if (ex instanceof UnknownHttpStatusCodeException) {
			final HttpStatus status = ((HttpStatusCodeException) ex).getStatusCode();
			final String responseBody = ((UnknownHttpStatusCodeException) ex).getResponseBodyAsString();
			printLog(String.valueOf(status.value()), ex, request, Level.ERROR);
			return new ResponseEntity<>(responseBody, prepareHeaders(new HttpHeaders()), status);
		}
		printLog("500", ex, request, Level.ERROR);
		return new ResponseEntity<>(RestResponse.failure(ResponseError.of(ex.getMessage())), prepareHeaders(new HttpHeaders()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * 参数校验异常处理。该类异常由框架层面统一触发和统一处理，返回状态码400
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//		ex.getBindingResult().getAllErrors().forEach(error -> {
//			// final String objectName = error.getObjectName();
//			final String defaultMessage = error.getDefaultMessage();
//			final String message = this.getLocalMessage(defaultMessage, defaultMessage);
//			// final String field = error.getCode();
//			errors.add(new ResponseError(defaultMessage, message, null));
//		});
		StringBuilder message = new StringBuilder();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			final String defaultMessage = error.getDefaultMessage();
			final String field = error.getField();
			if (message.length() > 0) {
				message.append(", ");
			}
			message.append("[").append(field).append("]:").append(defaultMessage);
		});
		RestResponse<Void> body = RestResponse.failure(ResponseError.of(message.toString()).code("400"));
		return handleExceptionInternal(ex, body, headers, status, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exceptionHandler(final Exception ex, final HttpServletRequest request) {
		final String method = request.getMethod();
		final String url = request.getRequestURL().toString();

		logger.error(method + " " + url + " 请求发生异常.", ex);
		HttpHeaders header = prepareHeaders(new HttpHeaders());
		final ResponseStatus statusAnnotation = ex.getClass().getAnnotation(ResponseStatus.class);
		if (statusAnnotation != null) {
			final HttpStatus status = statusAnnotation.value();
			return new ResponseEntity<>(RestResponse.failure(ResponseError.of(ex.getMessage())), header, status);
		}

		String exceptionName = ex.getClass().getName().toLowerCase();
		if (exceptionName.contains("authentication") || exceptionName.contains("authenticate")) {
			return new ResponseEntity<>(RestResponse.failure(ResponseError.of(ex.getMessage()).code("401")), header,
					HttpStatus.UNAUTHORIZED);
		}
		if (exceptionName.contains("authorization") || exceptionName.contains("accessdenied") || exceptionName.contains("accessdeny")) {
			return new ResponseEntity<>(RestResponse.failure(ResponseError.of(ex.getMessage()).code("403")), header,
					HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(RestResponse.failure(ResponseError.of(ex.getMessage()).code("500")), header, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected String getLocalMessage(final String code, final String defaultMsg, final Object... params) {
		final Locale local = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code, params, defaultMsg, local);
	}

	private void printLog(final String code, Exception ex, final HttpServletRequest request, Level level) {
		final String method = request.getMethod();
		final String url = request.getRequestURL().toString();
		String info = String.format("status code %s, reason %s, [%s] %s", code, ex.getMessage(), method, url);
		if (level == Level.DEBUG) {
			logger.debug(info, ex);
		} else if (level == Level.INFO) {
			logger.info(info, ex);
		} else if (level == Level.WARN) {
			logger.warn(info, ex);
		} else if (level == Level.ERROR) {
			logger.error(info, ex);
		}
	}
	
	private HttpHeaders prepareHeaders(HttpHeaders headers) {
		if (headers == null) {
			headers = new HttpHeaders();
		}
		if (headers.getContentType() == null || !headers.getContentType().equals(MediaType.APPLICATION_JSON_UTF8)) {
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		}
		return headers;
	}

	public static class HandlerCondition implements Condition {

		@Override
		public boolean matches(final ConditionContext paramConditionContext,
				final AnnotatedTypeMetadata paramAnnotatedTypeMetadata) {
			final String[] existingBeans = paramConditionContext.getBeanFactory()
					.getBeanNamesForType(RestResponseExceptionHandler.class);
			return (existingBeans == null) || (existingBeans.length == 0);
		}
	}

}
