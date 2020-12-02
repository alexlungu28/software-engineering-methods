package roomscheduler.communication;

import com.google.gson.Gson;
import javassist.bytecode.ExceptionTable;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerCommunication {

    static CredentialsProvider provider = new BasicCredentialsProvider();
    static CloseableHttpClient client;// = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    static Gson gson = new Gson();
    static HttpPost post = new HttpPost();

    /**
     * Get the json of the object from the serverpath.
     *
     * @param path : url path.
     * @return json of given object.
     */
    public static String getObjectJson(String port, String path) throws IOException {
        CloseableHttpResponse response = sendGetRequest(port, path);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * Sends a get request to the server to the specified path.
     *
     * @param path : url path.
     * @return the servers response
     */
    public static CloseableHttpResponse sendGetRequest(String port, String path) {
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        try (CloseableHttpResponse response = client.execute(new HttpGet("http://localhost:" + port + "/" + path));) {
            client.close();
            return response;
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
     * @param path : url path.
     * @param json : becomes the content of the Http request.
     * @return responsestatuscode : returns the status code.
     * @throws UnsupportedEncodingException when something goes wrong during encoding.
     */
    public static int executePostRequest(String port, String path, String json) throws UnsupportedEncodingException {
        try {
            post.setURI(new URI("http://localhost:" + port + "/" + path));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        post.setHeader("Content-type", "application/json");
        post.setEntity(new StringEntity(json));
        CloseableHttpResponse response = sendPostRequest(post);
        if (response == null) {
            return executePostRequest(port, path, json);
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
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        try (CloseableHttpResponse response = client.execute(post);) {
            client.close();
            return response;
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
     * @param path : url path.
     * @return : returns the status code.
     * @throws IOException : when something goes wrong with the IO operations.
     */
    public static int sendDeleteRequest(String port, String path) throws IOException {
        HttpDelete delete = new HttpDelete("http://localhost:" + port + "/" + path);
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        try (CloseableHttpResponse response = client.execute(delete)) {
            client.close();
            delete.reset();
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;

    }
}

