package com.bigthumb.honeytip.repository;

import static com.bigthumb.honeytip.domain.QCategory.category;

import com.bigthumb.honeytip.domain.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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

  public List<Category> findAll() {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(category).fetch();
  }

  public Category findById(Long id) {
    return em.find(Category.class, id);
  }

  public Category findByName(String name) {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(category).where(category.name.eq(name)).fetchOne();
  }

  public void delete(Category category) {
    em.remove(category);
  }
}
