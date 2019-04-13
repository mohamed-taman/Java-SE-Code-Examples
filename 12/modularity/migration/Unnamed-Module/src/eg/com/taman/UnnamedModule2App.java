package eg.com.taman;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class UnnamedModule2App {

    public static void main(String... args) {

        Logger logger = Logger.getLogger(UnnamedModule2App.class.getName());

        Module module = UnnamedModule2App.class.getModule();

        logger.log(INFO, "Module: {0} ", module);
    }
}
