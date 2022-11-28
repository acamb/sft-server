package ac.project.sft.configuration;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotAuthorizedException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.Error;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex){

        if(ex instanceof NotFoundException){
            return ResponseEntity.notFound().build();
        }
        if(ex instanceof BadRequestException){
            return new ResponseEntity(new Error(ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(ex instanceof NotAuthorizedException){
            return new ResponseEntity(new Error(ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(ex instanceof InternalAuthenticationServiceException || ex instanceof ExpiredJwtException){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.internalServerError().build();

    }
}
