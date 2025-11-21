package org.marakobz.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.marakobz.dto.ReviewDto;
import org.marakobz.model.Review;
import org.marakobz.service.ReservationService;
import org.marakobz.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dreams")
public class ReviewController {

    private ReviewService reviewService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public ResponseEntity<Object> submitReview(@RequestBody ReviewDto reviewDto) {
        try {
            logger.info("Submitting review: " + reviewDto);
            reviewService.submitReview(reviewDto);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при отправке отзыва");
        }
    }

}
