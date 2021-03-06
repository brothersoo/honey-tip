package com.bigthumb.honeytip.repository;

import static com.bigthumb.honeytip.domain.QUser.user;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserStatus;
import com.bigthumb.honeytip.domain.UserType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final EntityManager em;

  public void save(User user) {
    em.persist(user);
  }

  public User findById(Long id) {
    return em.find(User.class, id);
  }

  public User findByUsername(String username) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(user).where(user.username.eq(username)).fetchOne();
  }

  public List<User> findAll() {
    return em.createQuery("SELECT u from User u", User.class).getResultList();
  }

  public List<User> findAllMember() {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(user).where(user.type.eq(UserType.MEMBER)).fetch();
  }

  public User findByNickname(String nickname) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(user).where(user.nickname.eq(nickname)).fetchOne();
  }

  public List<User> findMemberByNickname(String keyword) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query
        .selectFrom(user)
        .where(
            user.nickname.likeIgnoreCase("%" + keyword + "%")
                .and(user.type.eq(UserType.MEMBER))
        )
        .fetch();
  }

  /**
   * Member quit by himself
   */
  public void remove(User user) {
    user.updateStatus(UserStatus.QUIT);
  }

  /**
   * Admin bans a member
   */
  public void ban(User user) {
    user.updateStatus(UserStatus.BANNED);
  }

  /**
   * Admin deletes a member
   */
  public void delete(User user) {
    em.remove(user);
  }
}
