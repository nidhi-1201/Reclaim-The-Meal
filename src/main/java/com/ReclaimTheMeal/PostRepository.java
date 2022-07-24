package com.ReclaimTheMeal;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long id);
    @Modifying
    @Query(value = "delete from posts p where p.end_time <= ?1", nativeQuery = true)
    public void deleteByDateTimeEquals(Date end_time);

    @Modifying
    @Query(value = "select * from posts p where p.end_time <= ?1", nativeQuery = true)
    public List<Post> selectByDateTimeEquals(Date end_time);
}
