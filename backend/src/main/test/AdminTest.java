import com.coursework.controller.AdminController;
import com.coursework.dto.ArchitectRequestDto;
import com.coursework.enums.AdminStatus;
import com.coursework.enums.ReservationStatus;
import com.coursework.service.AdminService;
import com.coursework.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTest {

    @Mock
    private AdminService adminService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getArchitectRequests_ShouldReturnList() {
        ArchitectRequestDto request1 = new ArchitectRequestDto();
        request1.setId(1L);
        request1.setUsername("arch1");

        ArchitectRequestDto request2 = new ArchitectRequestDto();
        request2.setId(2L);
        request2.setUsername("arch2");

        List<ArchitectRequestDto> requests = Arrays.asList(request1, request2);
        when(adminService.getArchitectRequests()).thenReturn(requests);
        ResponseEntity<List<ArchitectRequestDto>> response = adminController.getArchitectRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void approveArchitect_ShouldReturnOk() {
        Long architectId = 1L;
        doNothing().when(adminService).updateArchitectStatus(architectId, AdminStatus.APPROVED);

        ResponseEntity<Void> response = adminController.approveArchitect(architectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService, times(1)).updateArchitectStatus(architectId, AdminStatus.APPROVED);
    }

    @Test
    void denyReservation_ShouldReturnOk() {
        Long reservationId = 1L;
        doNothing().when(reservationService).updateResStatus(reservationId, ReservationStatus.DENIED);

        ResponseEntity<Void> response = adminController.denyReservation(reservationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reservationService, times(1)).updateResStatus(reservationId, ReservationStatus.DENIED);
    }
}