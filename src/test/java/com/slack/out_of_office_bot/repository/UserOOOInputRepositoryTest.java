package com.slack.out_of_office_bot.repository;

import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
class UserOOOInputRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserOOOInputRepository userOOOInputRepository;

    @Test
    void findUserInputBetween_shouldReturnCorrectUserWithLastTimeOutOfOffice() {
        String matchUserId = "123456";
        String missUserId = "987654";
        String matchUser = "Jane Doe";
        String missUser = "John Doe";
        String description = "Doctor appointment";
        int firstStartHour = 12;
        int firstEndHour = 15;
        int secondStartHour = 8;
        int secondEndHour = 10;
        LocalDateTime startDate = LocalDateTime.now().withHour(firstStartHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(firstEndHour).withMinute(0);

        LocalDateTime currentStartDate = DateTimeUtils.startOfDay();
        LocalDateTime currentEndDate = DateTimeUtils.endOfDay();

        UserOOOInput firstMatchUserOOOInput = createOOOInput(matchUserId, matchUser, description, startDate, endDate);
        UserOOOInput secondMatchUserOOOInput = createOOOInput(matchUserId, matchUser, description, startDate.withHour(secondStartHour), endDate.withHour(secondEndHour));
        UserOOOInput missUserOOOInput = createOOOInput(missUserId, missUser, description, startDate, endDate);
        UserOOOInput missTomorrowUserOOOInput = createOOOInput(missUserId, missUser, description, startDate.plusDays(1), endDate.plusDays(1));

        entityManager.persist(firstMatchUserOOOInput);
        entityManager.persist(secondMatchUserOOOInput);
        entityManager.persist(missUserOOOInput);
        entityManager.persist(missTomorrowUserOOOInput);
        entityManager.flush();

        Optional<List<UserOOOInput>> actualUserOOOInput = userOOOInputRepository.findUserInputBetween(matchUserId, currentStartDate, currentEndDate);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.get().size(), equalTo(2));
        assertThat(actualUserOOOInput.get().get(0).getSlackUserId(), is(equalTo(matchUserId)));
    }

    @Test
    void findUserOOOInputsBySlackUserId_shouldReturnCorrectUsers() {
        String matchUserId = "123456";
        String missUserId = "987654";
        String matchUser = "Jane Doe";
        String missUser = "John Doe";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);
        UserOOOInput matchUserOOOInput = createOOOInput(matchUserId, matchUser, description, startDate, endDate);
        UserOOOInput missUserOOOInput = createOOOInput(missUserId, missUser, description, startDate, endDate);

        entityManager.persist(matchUserOOOInput);
        entityManager.persist(missUserOOOInput);
        entityManager.flush();

        Optional<List<UserOOOInput>> foundUserOOOInput = userOOOInputRepository.findUserOOOInputsBySlackUserId(matchUserId);

        assertThat(foundUserOOOInput, is(notNullValue()));
        assertThat(foundUserOOOInput.get().size(), is(equalTo(1)));
        assertThat(foundUserOOOInput.get().get(0).getSlackUserId(), is(equalTo(matchUserId)));
    }

    @Test
    void findFirstBySlackUserIdOrderByEndTimeDesc_shouldGetCorrectUserWithMostRecentEndTime() {
        String matchUserId = "123456";
        String missUserId = "987654";
        String matchUser = "Jane Doe";
        String missUser = "John Doe";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);
        UserOOOInput matchUserOOOInput = createOOOInput(matchUserId, matchUser, description, startDate, endDate);
        UserOOOInput missUserOOOInput = createOOOInput(missUserId, missUser, description, startDate, endDate);

        entityManager.persist(matchUserOOOInput);
        entityManager.persist(missUserOOOInput);
        entityManager.flush();

        Optional<UserOOOInput> foundUserOOOInput = userOOOInputRepository.findFirstBySlackUserIdOrderByEndTimeDesc(matchUserId);

        assertThat(foundUserOOOInput, is(notNullValue()));
        assertThat(foundUserOOOInput.get().getSlackUserId(), is(equalTo(matchUserId)));
    }

    @Test
    void findUserOOOInputByStartTimeAndEndTime_shouldReturnCorrectUserWithAskedStartTimeAndEndTime() {
        String matchUserId = "123456";
        String missUserId = "987654";
        String matchUser = "Jane Doe";
        String missUser = "John Doe";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);
        UserOOOInput matchUserOOOInput = createOOOInput(matchUserId, matchUser, description, startDate, endDate);
        UserOOOInput missUserOOOInput = createOOOInput(missUserId, missUser, description, startDate.plusDays(1), endDate.plusDays(1));

        entityManager.persist(matchUserOOOInput);
        entityManager.persist(missUserOOOInput);
        entityManager.flush();

        Optional<UserOOOInput> foundUserOOOInput = userOOOInputRepository.findUserOOOInputByStartTimeAndEndTime(startDate, endDate);

        assertThat(foundUserOOOInput, is(notNullValue()));
        assertThat(foundUserOOOInput.get().getSlackUserId(), is(equalTo(matchUserId)));
        assertThat(foundUserOOOInput.get().getStartTime(), is(equalTo(startDate)));
        assertThat(foundUserOOOInput.get().getEndTime(), is(equalTo(endDate)));
    }

    private UserOOOInput createOOOInput(String userId, String username, String description, LocalDateTime startDate, LocalDateTime endDate) {
        return UserOOOInput.builder()
                .slackUserId(userId)
                .slackUsername(username)
                .description(description)
                .startTime(startDate)
                .endTime(endDate)
                .build();
    }
}