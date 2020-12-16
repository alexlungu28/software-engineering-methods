package roomscheduler.controllers;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Authorization {

    static CredentialsProvider provider = new BasicCredentialsProvider();
    static CloseableHttpClient client = HttpClientBuilder.create()
            .setDefaultCredentialsProvider(provider).build();

    /**
     * Method for checking whether the token given corresponds to a user with role 'role'.
     *
     * @param token token
     * @param role role
     * @return true if the user has that role, false otherwise
     */
    @SuppressWarnings("PMD")
    public static boolean authorize(String token, String role) {
        token = token.replace("Bearer ", "");
        CloseableHttpResponse response = sendGetRequest("8090", "is" + role, token);
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode == 200;
    }

    /**
     * Sends a get request to the server to the specified path.
     *
     * @param path : url path.
     * @return the servers response
     */
    public static CloseableHttpResponse sendGetRequest(String port, String path, String token) {
        try {
            HttpGet get = new HttpGet("http://localhost:" +
                    port + "/" + path);
            get.setHeader("Authorization", "Bearer " + token);
            return client.execute(get);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
