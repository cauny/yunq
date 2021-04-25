package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.AdminRoleToPer;
import com.ep.yunq.domain.entity.AdminUserToRole;
import com.ep.yunq.domain.entity.PermissionResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRoleToPerDAO extends JpaRepository<AdminRoleToPer,Integer> {


    @Query("select rs from PermissionResource rs, AdminRoleToPer bind " +
            "where rs.id = bind.permissionId and bind.roleId = ?1")
    List<PermissionResource> findAllResByRid(int rid);


    @Query("delete from AdminRoleToPer bind " +
            "where bind.roleId = ?1 and bind.permissionId = ?2")
    void deleteRoleResourceBind(int rid,int pid);

    void deleteAllByRoleId(int roleId);

    void deleteAllByPermissionId(int permissionId);

    List<AdminRoleToPer> findAllByRoleId(int roleId);

}

