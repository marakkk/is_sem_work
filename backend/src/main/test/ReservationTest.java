import com.coursework.controller.ReservationController;
import com.coursework.dto.*;
import com.coursework.model.*;
import com.coursework.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void createReservation_ShouldReturnCreated() {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDreamName("Test Dream");

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationService.createReservation(any(ReservationDto.class), any(HttpServletRequest.class))).thenReturn(reservation);

        ResponseEntity<?> response = reservationController.createReservation(reservationDto, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getCalendarEntries_ShouldReturnList() {
        String date = "2024-01-15";
        String time = "14:30";
        String status = "NOT_AVAILABLE";

        Calendar calendar = new Calendar();
        calendar.setId(1L);
        calendar.setDate(LocalDate.parse(date));
        calendar.setTime(LocalTime.parse(time));

        List<Calendar> calendars = Arrays.asList(calendar);
        when(reservationService.getCalendarEntries(date, time, status)).thenReturn(calendars);

        ResponseEntity<List<CalendarDto>> response = reservationController.getCalendarEntries(date, time, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getCalendarId());
    }
}