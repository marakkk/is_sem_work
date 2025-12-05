package com.university.coursework.controller;

import com.university.coursework.dto.ReviewDto;
import com.university.coursework.service.ReviewService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/dreams")
public class ReviewController {

    private final ReviewService reviewService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);


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
