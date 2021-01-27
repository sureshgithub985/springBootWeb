package com.ey.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ey.core.entity.Subscriber;

public interface SubscriberRepository
		extends JpaRepository<Subscriber, Long>, PagingAndSortingRepository<Subscriber, Long> {

	@Modifying
	@Query("update Subscriber sub set sub.suspended =:suspended where sub.mdn = :mdn")
	public int updateSubscriberSuspended(@Param("suspended") boolean suspended, @Param("mdn") long mdn);

	/*
	 * @Modifying
	 * 
	 * @Query(name = " insert into whlr_mdn(mdn) values (:mdn)", nativeQuery = true)
	 * public int insertSubscriberMdn(@Param("mdn") long mdn);
	 */

	/*
	 * @Modifying
	 * 
	 * @Query(name = " insert into shared_contacts(user_name) values (:user_name)",
	 * nativeQuery = true) public int insertSubscriberUserName(@Param("user_name")
	 * String user_name);
	 */

	public Subscriber findByMdn(Long mdn);

}
