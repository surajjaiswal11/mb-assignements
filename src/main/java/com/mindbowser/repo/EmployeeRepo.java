package com.mindbowser.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mindbowser.constant.QueryConstant;
import com.mindbowser.entity.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmailAndIsDeleted(String email, boolean b);

	Optional<Employee> findByIdAndIsDeleted(Long id, boolean b);

	@Query(value = QueryConstant.SELECT + QueryConstant.COUNT + QueryConstant.FROM
			+ QueryConstant.IS_DELETED_AND_ADDED_BY_AND_ORDER_BY_ID_DESC, nativeQuery = true)
	Object countByIdAndIsDeleted(Long id, boolean b);

	/**
	 * We used Native query because The findbymanager will be the problematic like
	 * we mapped long id and we are finding by manager or id so it would be return
	 * null.
	 * 
	 * @param id
	 * @param b
	 * @param currentPage
	 * @param totalPerPage
	 * @return
	 */
	@Query(value = QueryConstant.SELECT + QueryConstant.STAR + QueryConstant.FROM
			+ QueryConstant.IS_DELETED_AND_ADDED_BY_AND_ORDER_BY_ID_DESC + QueryConstant.LIMIT, nativeQuery = true)
	List<Employee> findByManagerAndIsDeleted(Long id, boolean b, Integer currentPage, Integer totalPerPage);

	@Query(value = QueryConstant.SELECT + QueryConstant.STAR + QueryConstant.FROM + QueryConstant.CURRENT_DATE
			+ QueryConstant.AND + QueryConstant.IS_DELETED_AND_ADDED_BY_AND_ORDER_BY_ID_DESC, nativeQuery = true)
	List<Employee> findByIsDeleted(Long id, boolean b);

}
