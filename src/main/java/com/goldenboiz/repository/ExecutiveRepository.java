package com.goldenboiz.repository;

import com.goldenboiz.model.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExecutiveRepository extends JpaRepository<Executive, Long> {
    List<Executive> findAllByOrderBySortOrderAsc();
}