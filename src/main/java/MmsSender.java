import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.verbs.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.twilio.sdk.TwilioRestException;
//import com.twilio.sdk.resource.factory.MessageFactory;
//import com.twilio.sdk.resource.instance.Message;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;

//import com.twilio.sdk.resource.instance.Message;

public class MmsSender {

    private static final String ACCOUNT_SID = "ACf6b08b21f93569046328af7fa30b125b";
    private static final String AUTH_TOKEN = "a7cf68154236ae5ae4845da84d1d5f21";

    public void send(String numberToSend, String imageUrl) throws TwilioRestException {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "+380935299155"));
        params.add(new BasicNameValuePair("From", "+14158004619"));
        params.add(new BasicNameValuePair("Body", imageUrl));
        params.add(new BasicNameValuePair("MediaUrl", "https://c1.staticflickr.com/3/2899/14341091933_1e92e62d12_b.jpg"));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        com.twilio.sdk.resource.instance.Message message = messageFactory.create(params);
    }

    public HttpServletResponse respondMessage(HttpServletResponse response) {
        TwiMLResponse twiml = new TwiMLResponse();
        Message message = new Message();
        try {
            message.append(new Body("The Robots are coming! Head for the hills!"));
            message.append(new Media("https://farm8.staticflickr.com/7090/6941316406_80b4d6d50e_z_d.jpg"));
            twiml.append(message);
        } catch (TwiMLException e) {
            e.printStackTrace();
        }

        response.setContentType("text/xml");
        try {
            response.getWriter().print(twiml.toXML());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
