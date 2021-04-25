package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.PermissionResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionResourceDAO extends JpaRepository<PermissionResource,Integer> {

    /* 根据代码查找权限资源 */
    PermissionResource findByCode(String code);

    /*  */
    PermissionResource findByName(String name);

    /*  */
    PermissionResource findById(int id);

    /* 查找所有可用权限路径 */
    @Query(nativeQuery = true,value = "select * from permisson where name='1'")
    Set<String> getAllEnableResourcePath();

    /* 查找所有不可用权限路径 */
    @Query(nativeQuery = true,value = "select * from permisson where name='2'")
    Set<String> getAllDisableResourcePath();

    /**
     * Get uri resource and resource-role relationship chain, eg: /api/v2/host===post===[role2,role3,role4]
     * @return resource-role chain set
     */
    @Query(value = "SELECT  CONCAT(LOWER(res.uri),\"===\",LOWER(res.method),\"===[\",IFNULL(GROUP_CONCAT(DISTINCT r.name),\"\"),\"]\") " +
            "FROM permission res " +
            "LEFT JOIN role_to_permission bind on res.id = bind.permission_id " +
            "LEFT JOIN role r on r.id = bind.role_id " +
            "where res.status = 1 " +
            "group by res.id", nativeQuery = true)
    Optional<List<String>> getEnableResourcePathRoleData();

    /**
     * Get disabled uri resources eg: /api/v2/host===post
     * @return resouce set
     */
    @Query("select CONCAT(LOWER(resource.uri),'===', resource.method) " +
            "from PermissionResource resource where resource.status = 9 order by resource.id")
    Optional<List<String>> getDisableResourcePathData();



    @Query(nativeQuery = true, value = "select * from permission where" +
            " name like ?1 or description like ?1 or uri like ?1 ")
    List<PermissionResource> search(String keyword1);

    /*List<PermissionResource> findAllByParentId(int parentId);*/

    /*@Query(nativeQuery = true, value = "select * from permission order by parent_id ")
    List<PermissionResource> findAllOrderByParentId();*/
}
