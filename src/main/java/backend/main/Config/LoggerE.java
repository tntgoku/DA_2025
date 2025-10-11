package backend.main.Config;

import java.util.logging.*;

public class LoggerE {
    public static Logger logger = Logger.getLogger(LoggerE.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                StackTraceElement ste = Thread.currentThread().getStackTrace()[8];
                return String.format(
                        "[%s] [%s:%d %s()] %s%n",
                        record.getLevel(),
                        ste.getFileName(),
                        ste.getLineNumber(),
                        ste.getMethodName(),
                        record.getMessage());
            }
        });
    }

    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    public static void error(String message, Throwable t) {
        logger.log(Level.SEVERE, message, t);
    }

    public static void waring(String message, Throwable t) {
        logger.log(Level.WARNING, message, t);
    }

    public static void config(String message, Throwable t) {
        logger.log(Level.CONFIG, message, t);
    }
}
