package xyz.mwszksnmdys.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MWSException extends RuntimeException{

    private Integer code;
    private String message;


}
