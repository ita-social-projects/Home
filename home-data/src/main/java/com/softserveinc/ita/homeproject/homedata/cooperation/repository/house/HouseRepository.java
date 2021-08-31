package com.softserveinc.ita.homeproject.homedata.cooperation.repository.house;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.entity.house.House;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends PagingAndSortingRepository<House, Long>, JpaSpecificationExecutor<House> {

    List<House> findHousesByCooperationId(Long id);

}
