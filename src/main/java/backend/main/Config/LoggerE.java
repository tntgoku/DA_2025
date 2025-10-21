package backend.main.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerE {

    public static final Logger logger = LoggerFactory.getLogger(LoggerE.class);

    // Để Bắt lỗi lấy log Debug dễ hơnhơn
    public static Logger getLogger() {
        // Lấy class gọi đến (caller)
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // [0] getStackTrace, [1] getLogger, [2] caller
        String className = stackTrace[2].getClassName();
        try {
            return LoggerFactory.getLogger(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return LoggerFactory.getLogger(LoggerE.class);
        }
    }
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

    public static void warning(String message) {
        StackTraceElement caller = getCaller();
        logger.warn(format(message, caller));
    }

    public static void error(String message, Throwable t) {
        StackTraceElement caller = getCaller();
        logger.error(format(message, caller), t);
    }

    public static void debug(String message) {
        StackTraceElement caller = getCaller();
        logger.debug(format(message, caller));
    }

    private static String format(String message, StackTraceElement ste) {
        return String.format("[%s:%d %s()] %s",
                ste.getFileName(),
                ste.getLineNumber(),
                ste.getMethodName(),
                message);
    }
}
