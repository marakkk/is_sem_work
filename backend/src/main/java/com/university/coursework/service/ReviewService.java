package com.university.coursework.service;

import jakarta.transaction.Transactional;
import com.university.coursework.dto.ReviewDto;
import com.university.coursework.model.Architect;
import com.university.coursework.model.Dream;
import com.university.coursework.model.Review;
import com.university.coursework.model.UsersDream;
import com.university.coursework.repository.ArchitectureRepository;
import com.university.coursework.repository.DreamRepository;
import com.university.coursework.repository.ReviewRepository;
import com.university.coursework.repository.UsersDreamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final ArchitectureRepository architectureRepository;

    private final UsersDreamRepository usersDreamRepository;

    private final DreamRepository dreamRepository;

    @Transactional
    public void submitReview(ReviewDto reviewRequestDto) {
        if (reviewRequestDto.getArchitectId() == null) {
            throw new IllegalArgumentException("architectId must not be null");
        }

        logger.info("Received review request: " + reviewRequestDto);

        Architect architect = architectureRepository.findById(reviewRequestDto.getArchitectId()).orElseThrow(() -> new IllegalArgumentException("Invalid architect ID"));

        logger.info("Found architect: " + architect);

        Dream dream = dreamRepository.findById(reviewRequestDto.getUsersDreamsId()).orElseThrow(() -> new IllegalArgumentException("Invalid dream ID"));

        logger.info("Found dream: " + dream);

        UsersDream usersDream = usersDreamRepository.findById(reviewRequestDto.getUsersDreamsId()).orElseThrow(() -> new IllegalArgumentException("UsersDream not found for the given dream"));

        logger.info("Found usersDream: " + usersDream);

        Review review = new Review();
        review.setArchitect(architect);
        review.setMark(reviewRequestDto.getMark());
        review.setUsersDream(usersDream);

        reviewRepository.save(review);
        logger.info("Review saved successfully: " + review);
    }
}
