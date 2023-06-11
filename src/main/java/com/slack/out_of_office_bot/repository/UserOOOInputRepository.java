package com.slack.out_of_office_bot.repository;

import com.slack.out_of_office_bot.model.UserOOOInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserOOOInputRepository extends JpaRepository<UserOOOInput, Long> {
    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM useroooinput u 
                    WHERE u.slack_user_id = :userId
                        AND (u.start_time BETWEEN :startDate AND :endDate
                        OR u.end_time BETWEEN :startDate AND :endDate
                        OR (u.start_time < :startDate AND u.end_time > :endDate))
                    ORDER BY u.start_time ASC""")
    Optional<List<UserOOOInput>> findUserInputBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<List<UserOOOInput>> findUserOOOInputsBySlackUserId(String userId);
    Optional<UserOOOInput> findFirstBySlackUserIdOrderByEndTimeDesc(String userId);
    Optional<UserOOOInput> findUserOOOInputByStartTimeAndEndTime(LocalDateTime startDate, LocalDateTime endDate);

}
