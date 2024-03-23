/*
 Title: ConsoleLogger
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debug.info;


import static engine.utils.EConstants.RESET;
import static engine.utils.EConstants.BLUE;
import static engine.utils.EConstants.GREEN;
import static engine.utils.EConstants.YELLOW;
import static engine.utils.EConstants.RED;


public class ConsoleLogger {

    public static void debug(String message) {
        System.out.println(BLUE + message + RESET);
    }

    public static void info(String message) {
        System.out.println(GREEN + message + RESET);
    }

    public static void warning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    public static void error(String message) {
        System.out.println(RED + message + RESET);
    }
}
/*End of ConsoleLogger class*/
