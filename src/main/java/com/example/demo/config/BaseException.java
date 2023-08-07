package com.example.demo.config;

import com.example.demo.enumpakage.Status;

public class BaseException extends RuntimeException {
    private Status status;
    private String message;
    private Throwable e;

    public BaseException(Status status) {
        super(status.getMsg());
        this.status = status;
        this.message = status.getMsg();
    }

    public BaseException(Status status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BaseException(Status status, Throwable cause) {
        super(status.getMsg(), cause);
        this.status = status;
        this.e = cause;
        this.message = status.getMsg();
    }

    public BaseException(Status status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
        this.e = cause;
    }

    public BaseException() {
    }

    public static BaseException of(Status status) {
        return new BaseException(status);
    }

    public static BaseException of(Status status, String message) {
        return new BaseException(status, message);
    }

    public static BaseException of(Status status, Throwable cause) {
        return new BaseException(status, cause);
    }

    public static BaseException of(Status status, String message, Throwable cause) {
        return new BaseException(status, message, cause);
    }

    public static BaseException of(Status status, Throwable cause, String msg) {
        return new BaseException(status, msg, cause);
    }

    public Status getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Throwable getE() {
        return this.e;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseException)) {
            return false;
        } else {
            BaseException other = (BaseException)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$status = this.getStatus();
                    Object other$status = other.getStatus();
                    if (this$status == null) {
                        if (other$status == null) {
                            break label47;
                        }
                    } else if (this$status.equals(other$status)) {
                        break label47;
                    }

                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$e = this.getE();
                Object other$e = other.getE();
                if (this$e == null) {
                    if (other$e != null) {
                        return false;
                    }
                } else if (!this$e.equals(other$e)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseException;
    }

    public int hashCode() {
//        int PRIME = true;
        int result = 1;
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $e = this.getE();
        result = result * 59 + ($e == null ? 43 : $e.hashCode());
        return result;
    }

    public String toString() {
        Status var10000 = this.getStatus();
        return "BaseException(status=" + var10000 + ", message=" + this.getMessage() + ", e=" + this.getE() + ")";
    }
}
