package com.org.coop.retail.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.coop.retail.entities.MaterialMaster;

public interface RetailMaterialMasterRepository extends JpaRepository<MaterialMaster, Integer> {
	@Query("select mm from MaterialMaster mm where mm.materialGroup.branchMaster.branchId = :branchId order by mm.materialGroup.materialGrpId, mm.createDate desc")
	public List<MaterialMaster> findAllMaterials(@Param("branchId") int branchId);
	
	@Query(value = "select count(*) from material_master where " +
				"exists(select * from stock_entry where material_id = :materialId limit 1) " +
				"and material_id = :materialId", nativeQuery=true)
	public int checkIfAnyChildRecordExists(@Param("materialId") int materialId);
}
