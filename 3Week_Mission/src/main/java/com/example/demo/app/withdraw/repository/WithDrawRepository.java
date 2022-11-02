package com.example.demo.app.withdraw.repository;

import com.example.demo.app.withdraw.entity.WithDraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithDrawRepository extends JpaRepository<WithDraw,Long> {
}
