package com.bigthumb.honeytip.repository;

import static com.bigthumb.honeytip.domain.QTip.tip;

import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.TipStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TipRepository {

  private final EntityManager em;

  public void save(Tip tip) {
    em.persist(tip);
  }

  public Tip findOne(Long id) {
    return em.find(Tip.class, id);
  }

  public List<Tip> findAll() {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(tip).fetch();
  }

  public List<Tip> findByCondition(String keyword) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    keyword = "%" + keyword + "%";
    return
        query
            .selectFrom(tip)
            .where(
                (tip.title.likeIgnoreCase(keyword)
                    .or(tip.user.nickname.likeIgnoreCase(keyword))
                    .or(tip.content.likeIgnoreCase(keyword)))
                    .and(tip.status.notIn(TipStatus.BAN, TipStatus.HID, TipStatus.RMV))
            ).fetch();
  }

  public void hide(Tip tip) {
    tip.updateStatus(TipStatus.HID);
  }

  /**
   * User cannot retrieve removed tip, but is not erased in database.
   * Only admin can erase or un-remove removed tip.
   */
  public void remove(Tip tip) {
    tip.updateStatus(TipStatus.RMV);
  }

  /**
   * Remove tip from service and database.
   */
  public void delete(Tip tip) {
    em.remove(tip);
  }
}
