package com.coursework.service;

import jakarta.transaction.Transactional;
import com.coursework.dto.ReviewDto;
import com.coursework.model.Architect;
import com.coursework.model.Dream;
import com.coursework.model.Review;
import com.coursework.model.UsersDream;
import com.coursework.repository.ArchitectureRepository;
import com.coursework.repository.DreamRepository;
import com.coursework.repository.ReviewRepository;
import com.coursework.repository.UsersDreamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    private final ReviewRepository reviewRepository;
    private final ArchitectureRepository architectureRepository;
    private final UsersDreamRepository usersDreamRepository;
    private final DreamRepository dreamRepository;

    public ReviewService(ReviewRepository reviewRepository, ArchitectureRepository architectureRepository, UsersDreamRepository usersDreamRepository, DreamRepository dreamRepository) {
        this.reviewRepository = reviewRepository;
        this.architectureRepository = architectureRepository;
        this.usersDreamRepository = usersDreamRepository;
        this.dreamRepository = dreamRepository;
    }

    @Transactional
    public void submitReview(ReviewDto reviewRequestDto) {
        if (reviewRequestDto.getArchitectId() == null) {
            throw new IllegalArgumentException("architectId must not be null");
        }

        logger.info("Received review request: " + reviewRequestDto);

        Architect architect = architectureRepository.findById(reviewRequestDto.getArchitectId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid architect ID"));

        logger.info("Found architect: " + architect);

        Dream dream = dreamRepository.findById(reviewRequestDto.getUsersDreamsId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid dream ID"));

        logger.info("Found dream: " + dream);

        UsersDream usersDream = usersDreamRepository.findById(reviewRequestDto.getUsersDreamsId())
                .orElseThrow(() -> new IllegalArgumentException("UsersDream not found for the given dream"));

        logger.info("Found usersDream: " + usersDream);

        Review review = new Review();
        review.setArchitect(architect);
        review.setMark(reviewRequestDto.getMark());
        review.setUsersDream(usersDream);

        reviewRepository.save(review);
        logger.info("Review saved successfully: " + review);
    }
}
