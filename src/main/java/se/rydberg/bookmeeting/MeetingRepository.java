package se.rydberg.bookmeeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

    @Query("SELECT m FROM Meeting m left JOIN FETCH m.meetingAnswers WHERE m.id = (:id)")
    Meeting getMeetingWithAnswers(@Param("id") UUID id);
    /**
     * @Query("SELECT s FROM ShopList s left JOIN FETCH s.articles WHERE s.id = (:id)")
     *     ShopList getShopListWithArticles(@Param("id") Integer id);
     *
     *     @Query("SELECT s FROM ShopList s left JOIN FETCH s.articles article WHERE s.id = (:id) order by article.category.title ASC")
     *     ShopList getShopListWithArticlesSortedByCategory(@Param("id") Integer id);
     */
}