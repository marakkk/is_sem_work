import com.coursework.controller.DreamController;
import com.coursework.dto.DreamDto;
import com.coursework.dto.DreamTemplateDto;
import com.coursework.model.Characters;
import com.coursework.service.DreamService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DreamTest {

    @Mock
    private DreamService dreamService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private DreamController dreamController;

    @Test
    void createOwnDream_ShouldReturnCreated() {
        DreamDto dreamDto = new DreamDto();
        dreamDto.setName("Test Dream");

        doNothing().when(dreamService).createOwnDream(any(DreamDto.class), any(HttpServletRequest.class));

        ResponseEntity<Object> response = dreamController.createOwnDream(dreamDto, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(dreamService, times(1)).createOwnDream(dreamDto, request);
    }

    @Test
    void getTemplateDreams_ShouldReturnList() {
        DreamTemplateDto template1 = new DreamTemplateDto();
        template1.setId(1L);
        template1.setName("Template 1");

        DreamTemplateDto template2 = new DreamTemplateDto();
        template2.setId(2L);
        template2.setName("Template 2");

        List<DreamTemplateDto> templates = Arrays.asList(template1, template2);
        when(dreamService.getTemplateDreams()).thenReturn(templates);

        List<DreamTemplateDto> result = dreamController.getTemplateDreams();

        assertEquals(2, result.size());
        assertEquals("Template 1", result.get(0).getName());
        assertEquals("Template 2", result.get(1).getName());
    }

    @Test
    void getCharactersByDreamId_ShouldReturnCharacters() {
        Long dreamId = 1L;
        Characters character1 = new Characters();
        character1.setName("Character 1");

        Characters character2 = new Characters();
        character2.setName("Character 2");

        List<Characters> characters = Arrays.asList(character1, character2);
        when(dreamService.getCharactersByDreamId(dreamId)).thenReturn(characters);

        ResponseEntity<List<Characters>> response = dreamController.getCharactersByDreamId(dreamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Character 1", response.getBody().get(0).getName());
    }
}