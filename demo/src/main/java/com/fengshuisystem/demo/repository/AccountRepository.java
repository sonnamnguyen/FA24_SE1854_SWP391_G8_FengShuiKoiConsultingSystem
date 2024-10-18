package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
