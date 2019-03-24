package eg.com.taman.support;

import eg.com.taman.renderer.SimpleRenderer;
import eg.org.taman.data.Message;

import static eg.org.taman.data.type.Type.*;

public class RendererSupport {

    private Message message = new Message();

    public void render(String message) {

        this.message.setMessage(message);
        this.message.setType(JSON);

        new SimpleRenderer().renderAsString(this.message);
    }

    public Message getCurrentMessage(){

        return this.message;
    }

}
