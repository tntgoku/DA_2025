package backend.main.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerE {

    private static final Logger logger = LoggerFactory.getLogger(LoggerE.class);

    /** 🔍 Lấy thông tin file, dòng, method của caller thật */
    private static StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : stackTrace) {
            if (!ste.getClassName().equals(LoggerE.class.getName()) &&
                !ste.getClassName().startsWith("java.lang.Thread")) {
                return ste;
            }
        }
        return stackTrace[stackTrace.length - 1];
    }

    /** 🟢 Info log */
    public static void info(String message) {
        StackTraceElement caller = getCaller();
        logger.info(format(message, caller));
    }

    /** 🟡 Warning log */
    public static void warning(String message) {
        StackTraceElement caller = getCaller();
        logger.warn(format(message, caller));
    }

    /** 🔴 Error log */
    public static void error(String message, Throwable t) {
        StackTraceElement caller = getCaller();
        logger.error(format(message, caller), t);
    }

    /** ⚙️ Config/Debug log */
    public static void debug(String message) {
        StackTraceElement caller = getCaller();
        logger.debug(format(message, caller));
    }

    /** 🧩 Định dạng log hiển thị */
    private static String format(String message, StackTraceElement ste) {
        return String.format("[%s:%d %s()] %s",
                ste.getFileName(),
                ste.getLineNumber(),
                ste.getMethodName(),
                message);
    }
}
