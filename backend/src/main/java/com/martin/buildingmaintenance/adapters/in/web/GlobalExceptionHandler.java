package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.exception.AccessDeniedException;
import com.martin.buildingmaintenance.application.exception.Auth0Exception;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest req) {
        log.error("NotFoundException at {}: {}", req.getRequestURI(), ex.toString(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage()
        );
        problem.setTitle("Resource Not Found");
        return problem;
    }

    @ExceptionHandler(Auth0Exception.class)
    public ProblemDetail handleAuth0(Auth0Exception ex, HttpServletRequest req) {
        log.error("Auth0Exception at {}: {}", req.getRequestURI(), ex.toString(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, ex.getMessage()
        );
        problem.setTitle("UNAUTHORIZED");
        return problem;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAuthZ(AccessDeniedException ex, HttpServletRequest req) {
        log.error("AccessDeniedException at {}: {}", req.getRequestURI(), ex.toString(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, ex.getMessage()
        );
        problem.setTitle("Forbidden");
        return problem;
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ProblemDetail handleSpringAccessDenied(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest req) {
        log.error("Spring AccessDeniedException at {}: {}", req.getRequestURI(), ex.toString(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, ex.getMessage()
        );
        problem.setTitle("Forbidden");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.error("MethodArgumentNotValidException at {}: {}", req.getRequestURI(), ex.toString(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed for one or more fields"
        );
        problem.setTitle("Invalid Request");

        ex.getBindingResult().getFieldErrors().forEach(err ->
                problem.<String, String>setProperty(err.getField(), err.getDefaultMessage())
        );

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception at {}: {}", req.getRequestURI(), ex.toString(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"
        );
        problem.setTitle("Internal Server Error");
        return problem;
    }


}
