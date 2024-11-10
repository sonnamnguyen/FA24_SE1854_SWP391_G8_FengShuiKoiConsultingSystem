package com.fengshuisystem.demo.repository;


import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.Role;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.enums.Status;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
    Optional<Account> findByUserName(String username);
    Optional<Account> findByEmail(String email);
    @Query(value = "SELECT r FROM Role r JOIN r.accounts u WHERE u.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Integer userId);

    @Query("SELECT ac FROM Account ac " +
            "WHERE ac.status = :status " +
            "AND (:name IS NULL OR TRIM(:name) = '' OR ac.userName LIKE %:name%)")
    Page<Account> findAllByUserNameContainingOriginContaining(String name, Status status, Pageable pageable);
    Page<Account> findAllByStatus(Status status, Pageable pageable);

    // 1. Người dùng mới hôm nay
    @Query("SELECT COUNT(a) FROM Account a INNER join Role r where r.name = 'USER' AND a.status = 'ACTIVE' AND FUNCTION('DATE', a.createdDate) = FUNCTION('DATE', CURRENT_TIMESTAMP)")
    long countNewUsersToday();

    // 2. Người dùng mới trong tuần này
    @Query("SELECT COUNT(a) FROM Account a INNER join Role r where r.name = 'USER' AND a.status = 'ACTIVE' AND FUNCTION('YEAR', a.createdDate) = FUNCTION('YEAR', CURRENT_TIMESTAMP) AND FUNCTION('WEEK', a.createdDate) = FUNCTION('WEEK', CURRENT_TIMESTAMP)")
    long countNewUsersThisWeek();

    // 3. Người dùng mới trong tháng này
    @Query("SELECT COUNT(a) FROM Account a INNER join Role r where r.name = 'USER' AND a.status = 'ACTIVE' AND FUNCTION('MONTH', a.createdDate) = FUNCTION('MONTH', CURRENT_TIMESTAMP) AND FUNCTION('YEAR', a.createdDate) = FUNCTION('YEAR', CURRENT_TIMESTAMP)")
    long countNewUsersThisMonth();


}