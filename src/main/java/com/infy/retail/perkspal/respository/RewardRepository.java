package com.infy.retail.perkspal.respository;

import com.infy.retail.perkspal.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward,Long> {
}
