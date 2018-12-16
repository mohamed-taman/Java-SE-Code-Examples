import static java.lang.System.*;

import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.*;
import java.io.IOException;
 
public class UsersHttpClient{
    
    public static void main(String[] args) throws Exception{
        var client = HttpClient.newBuilder().build();
        
        var request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://reqres.in/api/users?page=2"))
            .build();
 
        var response = 
            client.send(request, BodyHandlers.ofString());
        
        out.printf("Response code is: %d %n",response.statusCode());
        out.printf("The response body is:%n %s %n", response.body());     
    }
}