package eg.com.taman.calc;

public final class Calculator {

    public static double add(double x, double y){

        return x+y;
    }

    public static double divide(double x, double y){
        if(y == 0)
            throw new IllegalArgumentException("Y value can't be Zero");

        return (float)x/y;
    }

    public static double subtract(double x, double y){

        return x - y;
    }

    public static double multiply(double x, double y){

       return x * y;
    }


    public static void main(String[] args) {

        System.out.println(add(1.1,1.2));
        System.out.println(subtract(1.1,1.2));
        System.out.println(divide(1.1,1.2));
        System.out.println(multiply(1.1,1.2));
    }

}
