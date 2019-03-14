public class HelloWorld{
    public static void main(String[] args){
        if ( args == null || args.length < 1 ){
            System.err.println("Name required");
            System.exit(1);
        }

        var name = args[0];
        
        System.out.printf("Hello %s to IBM Developer World!! %n", name);
    }
}
