package com.project.onlinestore.buyer.repository;

import com.project.onlinestore.buyer.domain.Line;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends CrudRepository<Line,Long> {

}
