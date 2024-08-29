package com.dgp.web.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.dgp.common.code.BaseStatusCode;
import com.dgp.common.exception.BaseException;
import com.dgp.common.exception.NormalException;
import com.dgp.core.http.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NormalException.class)
    public BaseResponse normalExceptionHandler(HttpServletResponse response, NormalException ex) {
        logger.error(ex.getMessage(), ex);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(BaseStatusCode.BAD_REQUEST.getCode());
        baseResponse.message(ex.getMessage());
        return baseResponse;
    }

    @ExceptionHandler(BindException.class)
    public BaseResponse bindExceptionHandler(HttpServletResponse response, BindException ex) {
        return getBaseResponse(response, ex);
    }

    private BaseResponse getBaseResponse(HttpServletResponse response, BindException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String field = Objects.requireNonNull(fieldError).getField();
        String msg = fieldError.getDefaultMessage();
        return new BaseResponse(BaseStatusCode.PARAM_INVALID.getCode(), "[" + field + "]" + msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(HttpServletResponse response,
                                                               MethodArgumentNotValidException ex) {
        return getBaseResponse(response, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResponse methodArgumentTypeMismatchExceptionHandler(HttpServletResponse response,
                                                                   MethodArgumentTypeMismatchException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String field = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        String msg = "需要传入的类型：";
        return new BaseResponse(BaseStatusCode.PARAM_INVALID.getCode(),
                "[" + field + "]" + msg + requiredType.getTypeName());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse constraintViolationExceptionHandler(HttpServletResponse response,
                                                            ConstraintViolationException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().stream()
                .findFirst().get();
        List<Path.Node> pathList = StreamSupport.stream(
                        constraintViolation.getPropertyPath().spliterator(), false)
                .collect(Collectors.toList());
        String field = pathList.get(pathList.size() - 1).getName();
        String msg = constraintViolation.getMessage();
        return new BaseResponse(BaseStatusCode.PARAM_INVALID.getCode(), "[" + field + "]" + msg);
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse baseExceptionHandler(HttpServletResponse response, BaseException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode());
        return new BaseResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse anyExceptionHandler(HttpServletResponse response, Exception ex) {
        List<Throwable> throwableList = ExceptionUtil.getThrowableList(ex);
        if (CollUtil.isNotEmpty(throwableList)) {
            for (Throwable throwable : throwableList) {
                if (SQLException.class.isAssignableFrom(throwable.getClass())) {
                    logger.error(ex.getMessage(), ex);
                    response.setStatus(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode());
                    return new BaseResponse(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode(),
                            StrUtil.EMPTY);
                }
            }
        }
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new BaseResponse(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
    }

}
