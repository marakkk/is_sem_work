import com.coursework.controller.ReviewController;
import com.coursework.dto.ReviewDto;
import com.coursework.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void submitReview_ShouldReturnCreated() {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUsersDreamsId(1L);
        reviewDto.setArchitectId(2L);
        reviewDto.setMark(5);

        doNothing().when(reviewService).submitReview(any(ReviewDto.class));

        ResponseEntity<Object> response = reviewController.submitReview(reviewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(reviewService, times(1)).submitReview(reviewDto);
    }
}