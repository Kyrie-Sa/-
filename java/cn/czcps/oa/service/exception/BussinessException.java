package cn.czcps.oa.service.exception;
//异常处理类
/**
 * 自定义业务异常类，用于处理应用程序中特定的业务逻辑错误。
 * 继承自 RuntimeException，表示这是一种运行时异常，不需要在调用方法中声明抛出。
 */
public class BussinessException extends RuntimeException{
    private String code;
    private String message;

    public BussinessException(String code, String msg) {
        super(code+":"+msg);
        this.code = code;
        this.message=msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
