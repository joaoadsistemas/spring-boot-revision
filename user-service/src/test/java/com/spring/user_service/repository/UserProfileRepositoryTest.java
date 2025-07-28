package com.spring.user_service.repository;

import com.spring.user_service.config.IntegrationTestsConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserProfileRepositoryTest extends IntegrationTestsConfig {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    @DisplayName("findAllUsersByProfileId return a list with all users by profile id")
    @Sql(value = "/sql/profile/init_user_profile_2_users_1_profile.sql")
    void findAllUsersByProfileId_returnAllUsersByProfileId() {
        var profileId = 1L;
        var users = userProfileRepository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users).isNotEmpty()
                .hasSize(2)
                .doesNotContainNull();
    }

}
