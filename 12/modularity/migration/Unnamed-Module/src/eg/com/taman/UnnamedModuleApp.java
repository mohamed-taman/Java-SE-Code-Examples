package eg.com.taman;

import static java.lang.System.out;
import static java.util.Objects.isNull;

public class UnnamedModuleApp {

    public static void main(String... args) {

        Module module = UnnamedModuleApp.class.getModule();

        out.printf("Module: %s%nName: %s%nisNamed: %b%nDescriptor: %s%n",
                module,
                isNull(module.getName())? "Unnamed": module.getName(),
                module.isNamed(),
                isNull(module.getDescriptor())?
                       "Unnamed modules, doesn't has a Module descriptor" :
                        module.getDescriptor());
    }
}
