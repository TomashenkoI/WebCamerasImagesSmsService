import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

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
        Message message = messageFactory.create(params);
    }

}
