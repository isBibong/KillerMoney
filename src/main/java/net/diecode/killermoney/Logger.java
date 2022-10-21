package net.diecode.killermoney;

public class Logger {

    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("[Minecraft]");
    private static String consolePrefix = "[KillerMoney] ";
    
    public static void info(String message) {
        logger.info(consolePrefix + message);
    }

    public static void debug(String message) {
        logger.info(consolePrefix + "[DEBUG] " + message);
    }
    
    public static void warning(String message) {
        logger.warning(consolePrefix + message);
    }
}
