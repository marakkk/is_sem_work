import com.coursework.controller.AuthController;
import com.coursework.model.DreamUser;
import com.coursework.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_ShouldReturnCreated() {
        DreamUser user = new DreamUser();
        user.setUsername("testuser");
        user.setPassword("password123");

        doNothing().when(authService).register(any(DreamUser.class));

        ResponseEntity<Void> response = authController.register(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authService, times(1)).register(user);
    }

    @Test
    void login_ShouldReturnToken() {
        DreamUser user = new DreamUser();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(authService.login("testuser", "password123")).thenReturn("jwt-token-123");

        DreamUser loggedInUser = new DreamUser();
        loggedInUser.setUsername("testuser");
        loggedInUser.setRole(com.coursework.enums.Roles.CUSTOMER);

        when(authService.getUserByUsername("testuser")).thenReturn(loggedInUser);

        ResponseEntity<Map<String, Object>> response = authController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token-123", response.getBody().get("token"));
        assertEquals(com.coursework.enums.Roles.CUSTOMER, response.getBody().get("role"));
    }

    @Test
    void login_ShouldReturnUnauthorized() {
        DreamUser user = new DreamUser();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");

        when(authService.login("testuser", "wrongpassword")).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = authController.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}