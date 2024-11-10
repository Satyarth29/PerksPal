package com.infy.retail.perkspal.respository;

import com.infy.retail.perkspal.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward,Long> {
   public List<Reward> findAllByCustomerId(Long id);
}
