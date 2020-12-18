package op29sem58.room;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import op29sem58.room.communication.authorization.Authorization;
import op29sem58.room.communication.authorization.Role;
import op29sem58.room.controllers.RoomController;
import op29sem58.room.repositories.RoomRepository;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@SpringBootTest(classes = RoomService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AuthorizationTest {

    @Autowired
    private transient MockMvc mockMvc;
    final transient String authorization = "Authorization";
    final transient String bearer = "Bearer token";
    private static MockedStatic<Authorization> mockedAuth;

    @BeforeAll
    public static void mockAuthorization() {
        mockedAuth = Mockito.mockStatic(Authorization.class);
    }

    @AfterAll
    public static void closeMock() {
        mockedAuth.close();
    }

    @SuppressWarnings("PMD")
    @Test
    public void authorizeTestAdmin() throws Exception {

        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        final HttpEntity entity = Mockito.mock(HttpEntity.class);
        Mockito.when(response.getStatusLine()).thenReturn(new BasicStatusLine(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));

        mockedAuth.when(() -> Authorization.authorize("Bearer token", Role.Admin)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/allrooms")
                .header(authorization, bearer))
                .andExpect(status().isOk());
        Mockito.reset(entity, response);
    }

    @SuppressWarnings("PMD")
    @Test
    public void authorizeTestNotAdminBadRequest() throws Exception {
        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        Mockito.when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion
                .HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Not ok"));
        mockedAuth.when(
                () -> Authorization.authorize("Bearer token", Role.Admin)
        ).thenReturn(false);
        RoomRepository roomConMock = Mockito.mock(RoomRepository.class);
        Mockito.verify(roomConMock, Mockito.never()).findAll();
        RoomController r = Mockito.mock(RoomController.class);
        Mockito.when(r.getAllRooms(Mockito.anyString())).thenThrow(new RuntimeException());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/allrooms"))
                .andExpect(status().isBadRequest());
        Mockito.reset(response, roomConMock, r);
    }


}
