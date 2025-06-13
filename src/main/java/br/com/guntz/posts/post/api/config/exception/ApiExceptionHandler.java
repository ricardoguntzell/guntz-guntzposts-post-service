package br.com.guntz.posts.post.api.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.Objects;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handler(MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("One or more fields are invalid");
        problemDetail.setType(URI.create("/errors/fields-invalids"));

        var errors = ex.getFieldErrors().stream()
                .map(ErrorValidation::new)
                .toList();

        problemDetail.setProperty("fields", errors);

        return problemDetail;
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ProblemDetail handler(PostNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handler(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        String requiredTypeName = Objects.requireNonNull(ex.getRequiredType().getSimpleName());

        problemDetail.setTitle(String.format("Method Parameter: %s", ex.getName()));
        problemDetail.setDetail(String.format("is required type: %s", requiredTypeName));
        problemDetail.setType(URI.create("/errors/method-parameter"));

        return problemDetail;
    }

    private record ErrorValidation(String field, String message) {

        public ErrorValidation(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

}
