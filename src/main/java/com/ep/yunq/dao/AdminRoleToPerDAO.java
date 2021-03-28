package com.ep.yunq.dao;

import com.ep.yunq.pojo.AdminUserToRole;
import com.ep.yunq.pojo.PermissionResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRoleToPerDAO extends JpaRepository<AdminUserToRole,Integer> {


    @Query("select rs from PermissionResource rs, AdminRoleToPer bind " +
            "where rs.id = bind.pid and bind.rid = ?1")
    List<PermissionResource> findAllResByRid(int rid);


    @Query("delete from AdminRoleToPer bind " +
            "where bind.rid = ?1 and bind.pid = ?2")
    void deleteRoleResourceBind(int rid,int pid);

}

