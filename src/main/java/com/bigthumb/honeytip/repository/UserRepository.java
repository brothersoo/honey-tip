package com.bigthumb.honeytip.repository;

import static com.bigthumb.honeytip.domain.QUser.user;

import com.bigthumb.honeytip.domain.QUser;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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

  public User findByEmail(String email) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(user).where(user.email.eq(email)).fetchOne();
  }

  public List<User> findAll() {
    return em.createQuery("SELECT u from User u", User.class).getResultList();
  }

  public List<User> findByNickName(String nickName) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(user).where(user.nickname.likeIgnoreCase("%" + nickName + "%")).fetch();
  }

  /**
   * Member quit by himself
   */
  public void remove(User user) {
    user.updateStatus(UserStatus.QUT);
  }

  /**
   * Admin bans a member
   */
  public void ban(User user) {
    user.updateStatus(UserStatus.BAN);
  }

  /**
   * Admin deletes a member
   */
  public void delete(User user) {
    em.remove(user);
  }
}
