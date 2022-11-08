package xyz.mwszksnmdys.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import xyz.mwszksnmdys.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    public static void out(HttpServletResponse response, Result r) {
        ObjectMapper mapper = new ObjectMapper();

        /*APPLICATION_JSON_UTF8_VALUE deprecated major browsers like Chrome now comply with the specification
        and interpret correctly UTF-8 special characters without requiring a charset=UTF-8 parameter.*/
//        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}