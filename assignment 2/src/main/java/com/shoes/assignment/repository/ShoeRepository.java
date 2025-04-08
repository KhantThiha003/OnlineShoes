package com.shoes.assignment.repository;

import com.shoes.assignment.model.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    List<Shoe> findByName(String name);
}
