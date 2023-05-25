package ru.practicum.statsserverapp.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handler(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationException(HttpServletRequest request, final NotFoundException e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationException(HttpServletRequest request, final ValidateTimeException e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationParametersException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler({EndpointEmptyUriException.class, EndpointEmptyUriException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadRequestFoundException(final RuntimeException e) {
        log.info(e.getMessage(), e);
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info(e.getMessage(), e);
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST, e.getStackTrace());
    }
}
