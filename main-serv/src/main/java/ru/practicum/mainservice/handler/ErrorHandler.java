package ru.practicum.mainservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerValidationException(HttpServletRequest request, final Throwable e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerViolationException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(),BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationUnexpectedTypeExceptionException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationParametersException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerMessageNotReadException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PropertyValueException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationParametersValueException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerValidationValueSQLException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(NotValidatedExceptionConflict.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handlerNotFoundException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), CONFLICT);
    }


    @ExceptionHandler({EmptyResultDataAccessException.class, DataIntegrityViolationException.class})
    @ResponseStatus(CONFLICT)
    public ErrorResponse handlerUniqException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handlerDtoException(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handlerNotFoundException(HttpServletRequest request, final NotFoundException e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalException(HttpServletRequest request,
                                                  final HttpServerErrorException.InternalServerError e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerServerErrorMessage(HttpServletRequest request, final SQLException e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleAlreadyExistException(HttpServletRequest request, final IllegalStateException e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
