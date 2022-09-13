package com.mealfit.user.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.common.event.infrastructure.EventsPublisher;
import com.mealfit.exception.user.UserNotFoundException;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicUpdate
@Table(name = "users", indexes = {
      @Index(columnList = "username"),
      @Index(columnList = "nickname"),
      @Index(columnList = "email")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LoginInfo loginInfo;

    @Embedded
    private UserProfile userProfile;

    private double goalWeight;

    @Embedded
    private FastingTime fastingTime;

    @Embedded
    private Nutrition nutrition;

    @Embedded
    private UserStatusInfo userStatusInfo;

    public User(LoginInfo loginInfo, UserProfile userProfile, double goalWeight,
          FastingTime fastingTime, Nutrition nutrition, UserStatusInfo userStatusInfo) {
        this.loginInfo = loginInfo;
        this.userProfile = userProfile;
        this.goalWeight = goalWeight;
        this.fastingTime = fastingTime;
        this.nutrition = nutrition;
        this.userStatusInfo = userStatusInfo;
    }

    public User(Long id, LoginInfo loginInfo, UserProfile userProfile, double goalWeight,
          FastingTime fastingTime, Nutrition nutrition, UserStatusInfo userStatusInfo) {
        this.id = id;
        this.loginInfo = loginInfo;
        this.userProfile = userProfile;
        this.goalWeight = goalWeight;
        this.fastingTime = fastingTime;
        this.nutrition = nutrition;
        this.userStatusInfo = userStatusInfo;
    }

    public static User createLocalUser(LoginInfo loginInfo, UserProfile userBasicProfile,
          double goalWeight, FastingTime fastingTime) {

        EventsPublisher.raise(new SignupEvent(loginInfo.getUsername(),
              userBasicProfile.getEmail()));

        return new User(loginInfo,
              userBasicProfile,
              goalWeight,
              fastingTime,
              new Nutrition(0, 0, 0, 0),
              new UserStatusInfo(UserStatus.NOT_VALID, ProviderType.LOCAL));
    }

    public static User createSocialUser(LoginInfo loginInfo,
          UserProfile userProfile, ProviderType providerType) {
        return new User(loginInfo,
              userProfile,
              0,
              new FastingTime(null, null),
              new Nutrition(0, 0, 0, 0),
              new UserStatusInfo(UserStatus.FIRST_LOGIN, providerType));
    }

    public void changePassword(String password) {
        this.loginInfo = new LoginInfo(getLoginInfo().getUsername(), password);
    }

    public void changeNickname(String nickname) {
        checkUserDeleted();
        setUserProfile(new UserProfile(nickname,
              getUserProfile().getEmail(),
              getUserProfile().getProfileImage()));
    }

    public void changeProfileImage(String profileImage) {
        checkUserDeleted();
        setUserProfile(new UserProfile(
              getUserProfile().getNickname(),
              getUserProfile().getEmail(),
              profileImage));
    }

    public void changeGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }



    @Generated
    private void setUserProfile(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile is null");
        }
        this.userProfile = userProfile;
    }

    @Generated
    private void setUserStatusInfo(UserStatusInfo userStatusInfo) {
        if (userStatusInfo == null) {
            throw new IllegalArgumentException("userBasicProfile is null");
        }

        this.userStatusInfo = userStatusInfo;
    }

    public void changeFastingTime(FastingTime fastingTime) {
        setFastingTime(fastingTime);
    }

    @Generated
    private void setFastingTime(FastingTime fastingTime) {
        if (fastingTime == null) {
            throw new IllegalArgumentException("fastingTime is null");
        }

        this.fastingTime = fastingTime;
    }

    public void changeNutrition(Nutrition nutrition) {
        setNutrition(nutrition);
    }

    @Generated
    private void setNutrition(Nutrition nutrition) {
        if (nutrition == null) {
            throw new IllegalArgumentException("nutrition is null");
        }

        this.nutrition = nutrition;
    }

    public void changeUserStatus(UserStatus userStatus) {
        this.userStatusInfo = new UserStatusInfo(userStatus, this.userStatusInfo.getProviderType());
    }

    @Generated
    public boolean isNotValid() {
        return this.userStatusInfo.getUserStatus() == UserStatus.NOT_VALID;
    }

    @Generated
    public boolean isFirstLogin() {
        return this.userStatusInfo.getUserStatus() == UserStatus.FIRST_LOGIN;
    }

    @Generated
    public boolean isSocialUser() {
        return this.userStatusInfo.getProviderType() != ProviderType.LOCAL;
    }

    private void checkUserDeleted() {
        if (userStatusInfo.getUserStatus() == UserStatus.DELETE) {
            throw new UserNotFoundException("이미 회원탈퇴한 회원입니다. userId = " + this.id);
        }
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(id);
    }
}
