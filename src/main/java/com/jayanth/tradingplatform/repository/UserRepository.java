package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
