package com.bigthumb.honeytip.repository;

import static com.bigthumb.honeytip.domain.QCategory.category;

import com.bigthumb.honeytip.domain.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

  private final EntityManager em;

  public void save(Category category) {
    em.persist(category);
  }

  public Category findByName(String name) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(category).where(category.name.eq(name)).fetchOne();
  }
}
