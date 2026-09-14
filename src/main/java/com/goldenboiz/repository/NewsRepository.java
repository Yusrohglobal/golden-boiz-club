package com.goldenboiz.repository;

import com.goldenboiz.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByDatePostedDesc(); // Latest first
    List<News> findByCategoryOrderByDatePostedDesc(String category);
}