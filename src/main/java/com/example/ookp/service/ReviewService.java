package com.example.ookp.service;

import com.example.ookp.dto.ReviewDTO;
import com.example.ookp.mapper.ReviewMapper;
import com.example.ookp.model.Review;
import com.example.ookp.repository.ReviewRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public void add(ReviewDTO reviewDTO) {
        reviewDTO.setReviewDate(new Date());
        reviewRepository.save(reviewMapper.toReview(reviewDTO));
    }
}
