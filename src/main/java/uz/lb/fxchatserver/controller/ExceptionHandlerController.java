package uz.lb.fxchatserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.lb.fxchatserver.dto.ResultDTO;
import uz.lb.fxchatserver.exception.AppBadRequestException;
import uz.lb.fxchatserver.exception.ItemNotFoundException;

@ControllerAdvice
public class ExceptionHandlerController {
    private final ResultDTO result = new ResultDTO();

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResultDTO> handler(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result.error(e));
    }

    @ExceptionHandler({AppBadRequestException.class})
    public ResponseEntity<ResultDTO> handlerBadRequest(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(result.error(e));
    }

    @ExceptionHandler({ItemNotFoundException.class})
    public ResponseEntity<ResultDTO> handlerNotFound(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(result.error(e));
    }

}
