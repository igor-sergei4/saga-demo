package com.github.igorsergei4.sagademo.common.config;

import com.github.igorsergei4.sagademo.common.dto.ErrorResponseDto;
import com.github.igorsergei4.sagademo.common.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class CommonExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponseDto handleEntityNotFoundException(EntityNotFoundException exception) {
        LOGGER.warn(exception.getMessage());
        return new ErrorResponseDto(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().stream().collect(Collectors.toMap(
                error -> ((FieldError) error).getField(),
                error -> Optional.ofNullable(error.getDefaultMessage()).orElse("некорректное значение")
        ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorResponseDto handleMissingRequestParamException(MissingServletRequestParameterException exception) {
        return new ErrorResponseDto("Не указан обязательный параметр " + exception.getParameterName());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponseDto handleWrongRequestParamTypeException(MethodArgumentTypeMismatchException exception) {
        Parameter parameter = exception.getParameter().getParameter();
        return new ErrorResponseDto("Параметр " + parameter.getName() + " должен соответствовать формату " + parameter.getType());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String, List<String>> handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
        return exception.getParameterValidationResults().stream().collect(Collectors.toMap(
                parameterValidationResult ->
                        parameterValidationResult.getMethodParameter().getParameter().getName(),
                parameterValidationResult ->
                        parameterValidationResult.getResolvableErrors().stream()
                                .map(error ->
                                        Optional.ofNullable(error.getDefaultMessage()).orElse("некорректное значение")
                                )
                                .toList()
        ));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto handleGeneralException(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        return new ErrorResponseDto("Возникла ошибка при обработке запроса.");
    }
}
