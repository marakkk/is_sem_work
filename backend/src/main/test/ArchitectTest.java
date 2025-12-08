import com.coursework.controller.ArchitectController;
import com.coursework.dto.ArchitectDto;
import com.coursework.dto.DreamDto;
import com.coursework.model.Dream;
import com.coursework.service.ArchitectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchitectTest {

    @Mock
    private ArchitectService architectService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ArchitectController architectController;

    @Test
    void updateDreamPrice_ShouldReturnUpdatedDream() {
        Long dreamId = 1L;
        int price = 100;
        Dream dream = new Dream();
        dream.setId(dreamId);
        dream.setPrice(price);

        when(architectService.updateDreamPrice(dreamId, price)).thenReturn(dream);

        ResponseEntity<Dream> response = architectController.updateDreamPrice(dreamId, price);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(price, response.getBody().getPrice());
    }

    @Test
    void createTemplate_ShouldReturnCreated() {
        DreamDto dreamDto = new DreamDto();
        dreamDto.setName("Test Template");

        doNothing().when(architectService).createTemplate(any(DreamDto.class), any(HttpServletRequest.class));

        ResponseEntity<Object> response = architectController.createTemplate(dreamDto, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getAllArchitects_ShouldReturnList() {
        ArchitectDto architect1 = new ArchitectDto(1L, "architect1", 100, 5);
        ArchitectDto architect2 = new ArchitectDto(2L, "architect2", 150, 4);

        List<ArchitectDto> architects = Arrays.asList(architect1, architect2);
        when(architectService.getAllArchitects()).thenReturn(architects);

        List<ArchitectDto> result = architectController.getAllArchitects();

        assertEquals(2, result.size());
        assertEquals("architect1", result.get(0).getUsername());
        assertEquals(150, result.get(1).getPrice());
    }
}