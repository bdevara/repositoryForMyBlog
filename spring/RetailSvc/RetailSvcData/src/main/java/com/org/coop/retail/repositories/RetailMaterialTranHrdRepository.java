package com.org.coop.retail.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.coop.retail.entities.MaterialTranHrd;

public interface RetailMaterialTranHrdRepository extends JpaRepository<MaterialTranHrd, Integer> {
	
	@Query("select mth from MaterialTranHrd mth where mth.branchMaster.branchId = :branchId and mth.billNo = :billNo")
	public MaterialTranHrd findByBranchIdAndBillNo(@Param("branchId")int branchId, @Param("billNo")String billNo);
}
