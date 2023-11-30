package com.example.ookp.mapper;

import com.example.ookp.dto.ReviewDTO;
import com.example.ookp.model.Review;
import com.example.ookp.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final UserService userService;

    public Review toReview(ReviewDTO reviewDTO) {
        var review = new Review();
        review.setId(review.getId());
        if(reviewDTO.getUserId() == 0) {
            review.setUser(null);
        } else {
            review.setUser(userService.findById(reviewDTO.getUserId()));
        }
        review.setReviewText(reviewDTO.getReviewText());
        review.setReviewDate(reviewDTO.getReviewDate());
        return review;
    }

    public ReviewDTO toReviewDTO(Review review) {
        var reviewDTO = new ReviewDTO();
        reviewDTO.setId(reviewDTO.getId());
        if(review.getUser() == null) {
            reviewDTO.setUserId(0);
        } else {
            reviewDTO.setUserId(review.getUser().getId());
        }
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setReviewDate(review.getReviewDate());
        return reviewDTO;
    }
}
