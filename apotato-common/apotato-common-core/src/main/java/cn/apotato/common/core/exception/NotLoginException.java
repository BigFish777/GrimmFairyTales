package cn.apotato.common.core.exception;

/**
 * 未登录异常
 *
 * @author 胡晓鹏
 * @date 2023/04/28
 */
public class NotLoginException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NotLoginException(String message) {
        super(message);
    }
}
