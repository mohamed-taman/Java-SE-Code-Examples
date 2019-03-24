package eg.com.taman.main;

import eg.com.taman.support.RendererSupport;

 import eg.org.taman.data.Message;

 //import eg.com.taman.renderer.*;

 // import eg.org.taman.data.type.Type;

public class Client {

    public static void main(String[] args) {

        RendererSupport support = new RendererSupport();

        support.render("Welcome to the Java 12 Platform Module System");

        System.out.printf("%n %s %n %s","-----------------",support.getCurrentMessage().getMessage());

        System.out.printf("%n %s %n %s %n","-----------------",support.getCurrentMessage().getType());


        Message msg = support.getCurrentMessage();

        System.out.printf("%n %s %n %s","-----------------",msg.getMessage());

        System.out.printf("%n %s %n %s","-----------------",msg.getType());

    }
}
