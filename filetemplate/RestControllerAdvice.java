package ${PACKAGE_NAME};


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ${NAME} {


    @ExceptionHandler(Exception.class)
    public ResponseEntity handleNotFoundExcpetion(Exception ex){
        ResponseEntity rs = ResponseEntity.status(500).body(ex.getCause());
        return rs;
    }
}
