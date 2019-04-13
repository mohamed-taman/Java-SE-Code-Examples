package eg.com.taman.calc.app;

import eg.com.taman.calc.Calculator;

public class ModuleAnalyzer {


    public static void main(String[] args) {
        classAnalyzer(CalcApplication.class);
        System.out.println("----------------------");
        classAnalyzer(Calculator.class);
    }


    private static void classAnalyzer(Class<?> clazz) {
        System.out.println("class: " + clazz);

        Module module = clazz.getModule();

        System.out.printf("Module: %s\n Name: %s\n isNamed: %s\n " +
                          "Descriptor: %s\n " +
                          "isAutomatic: %s\n",module,
                               module.getName(),
                               module.isNamed(),
                               module.getDescriptor(),
                               module.getDescriptor().isAutomatic());
    }
}
