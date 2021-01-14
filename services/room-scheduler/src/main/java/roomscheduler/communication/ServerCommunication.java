package roomscheduler.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class ServerCommunication {

    static CredentialsProvider provider = new BasicCredentialsProvider();
    static CloseableHttpClient client = HttpClientBuilder.create()
            .setDefaultCredentialsProvider(provider).build();

    /**
     * Get the json of the object from the serverpath.
     *
     * @param req : serverpath.
     * @return json of given object.
     */
    @SuppressWarnings("PMD")
    public static String getObjectJson(HttpGet req) throws IOException {
        CloseableHttpResponse response = sendGetRequest(req);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * Sends a get request to the server to the specified path.
     *
     * @param req : serverpath.
     * @return the servers response
     */
    public static CloseableHttpResponse sendGetRequest(HttpGet req) {
        try {
            return client.execute(req);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes a post request to the serverpath with the json as entity.
     *
     * @param req : URI.
     * @param se : string entity.
     * @return responsestatuscode : returns the status code.
     * @throws UnsupportedEncodingException when something goes wrong during encoding.
     */
    @SuppressWarnings("PMD")
    public static int executePostRequest(URI req,
                                         StringEntity se) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost();
        post.setURI(req);
        post.setHeader("Content-type", "application/json");
        post.setEntity(se);
        CloseableHttpResponse response = sendPostRequest(post);
        if (response == null) {
            return executePostRequest(req, se);
        }
        return response.getStatusLine().getStatusCode();
    }

    /**
     * Sends a post request to the server.
     *
     * @param post : Http request which gets send to the server.
     * @return the servers response.
     */
    public static CloseableHttpResponse sendPostRequest(HttpPost post) {
        try {
            return client.execute(post);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.reset();
        }
        return null;
    }

    /**
     * Sends a delete request to the server.
     *
     * @param req : serverpath.
     * @return : returns the status code.
     * @throws IOException : when something goes wrong with the IO operations.
     */
    @SuppressWarnings("PMD")
    public static int sendDeleteRequest(HttpDelete req) throws IOException {
        try {
            CloseableHttpResponse response = client.execute(req);
            req.reset();
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }
}




