package eg.com.taman;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class PersonConverter {

    public static void main(String... st) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader("<person><name>Mohamed Taman</name></person>");
        Person person = (Person) unmarshaller.unmarshal(reader);
        System.out.println("\n" + person);
    }
}
