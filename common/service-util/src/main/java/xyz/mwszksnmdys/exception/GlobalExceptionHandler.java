package xyz.mwszksnmdys.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.result.ResultCodeEnum;



@ControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局异常处理");
    }
    // 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error1(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行特定异常处理");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e){
        e.printStackTrace();
        return Result.fail().code(ResultCodeEnum.PERMISSION.getCode()).message("没有权限");
    }
    // 自定义异常处理
    @ExceptionHandler(MWSException.class)
    @ResponseBody
    public Result error2(MWSException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }
}
