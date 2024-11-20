package com.fpoly.java5.jpa;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.java5.entities.User;

@Repository
public interface UserJPA extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> getAllUserByActive();

    @Query("SELECT u FROM User u")
    Page<User> getPageAllUser(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT('%', :keyword, '%') OR u.phone LIKE CONCAT('%', :keyword, '%') OR u.email LIKE CONCAT('%', :keyword, '%') OR u.username LIKE CONCAT('%', :keyword, '%')")
    Page<User> getPageSearchUserByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.active = true")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.active = true")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.phone = ?1 AND u.active = true")
    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.id <> ?1 AND u.username = ?2")
    Optional<User> checkUserNameWhereId(Integer id, String username);

    @Query("SELECT u FROM User u WHERE u.id <> ?1 AND u.phone = ?2")
    Optional<User> checkPhoneWhereId(Integer id, String phone);

    @Query("SELECT u FROM User u WHERE u.id <> ?1 AND u.email = ?2")
    Optional<User> checkEmailWhereId(Integer id, String email);

    // admin
    @Query("SELECT u FROM User u WHERE u.roles = false ORDER BY u.id DESC")
    Page<User> pageAllUserByRolesFalse(Pageable pageable);

    @Query("SELECT u FROM User u WHERE(:keyword is null OR u.name Like %:keyword% OR u.username Like %:keyword% OR u.email Like %:keyword% OR u.phone Like %:keyword%) AND (:status IS NULL OR u.active = :status) AND u.roles = false ORDER BY u.id DESC")
    Page<User> pageAllFilterByRolesFalse(@Param("keyword") String keyword, @Param("status") Boolean status,
            Pageable pageable);

    @Query("SELECT u FROM User u  WHERE u.username = ?2 AND (u.id <> ?1 OR ?1 IS NULL)")
    Optional<User> findByUsernameExist(Integer id, String name);
    @Query("SELECT u FROM User u  WHERE u.email = ?2 AND (u.id <> ?1 OR ?1 IS NULL)")
    Optional<User> findByEmailExist(Integer id, String email);
    @Query("SELECT u FROM User u  WHERE u.phone = ?2 AND (u.id <> ?1 OR ?1 IS NULL)")
    Optional<User> findByPhoneExist(Integer id, String phone);
}
