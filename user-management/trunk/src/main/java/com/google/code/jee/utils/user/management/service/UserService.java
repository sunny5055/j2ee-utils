package com.google.code.jee.utils.user.management.service;

import java.util.List;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.model.User;

/**
 * The Interface UserService.
 */
public interface UserService extends GenericService<Integer, User> {

	/**
     * Test the existence of an element with the parameter 'login'.
     * 
     * @param login the login
     * @return true, if success
     */
	public boolean existWithLogin(String login);

    /**
     * Search an element by its login.
     * 
     * @param login the login
     * @return the user
     */
	public User findByLogin(String login);
	
	/**
     * Test the existence of an element with the parameter 'roleName'.
     * 
     * @param roleName the roleName
     * @return true, if success
     */
	public boolean existWithRoleName(String roleName);

    /**
     * Search an element by its roleName.
     * 
     * @param roleName the roleName
     * @return the role
     */
	public Role findByRoleName(String roleName);
	
	/**
     * Test the existence of an element with the parameter 'rightName'.
     * 
     * @param rightName the rightName
     * @return true, if success
     */
	public boolean existWithRightName(String rightName);

    /**
     * Search an element by its rightName.
     * 
     * @param rightName the rightName
     * @return the right
     */
	public Right findByRightName(String rightName);
	
	/**
     * Search all the roles.
     * 
     * @return the list
     */
	public List<Role> findAllRoles();
	
	/**
     * Search all the rights.
     * 
     * @return the list
     */
	public List<Right> findAllRights();

    /**
     * Test the existence of a role with the parameter 'name' and its
     * user id.
     * 
     * @param userId the userId
     * @param roleName the role name
     * @return true, if success
     */
	public boolean existWithUserIdAndRoleName(Integer userId, String roleName);

    /**
     * Search a role with its name and its user primary key.
     * 
     * @param userId the userId
     * @param roleName the role name
     * @return the role
     */
	public Role findByUserIdAndRoleName(Integer userId, String roleName);

    /**
     * Count a user numbers of roles.
     * 
     * @param userId the userId
     * @return the number of roles corresponding to the user id
     */
	public Integer countForUserId(Integer userId);

    /**
     * Search all the user's roles.
     * 
     * @param userPrimaryKey the user primary key
     * @return the list
     */
    List<Role> findAllByUserId(Integer userId);
    
    /**
     * Test the existence of a right with the parameter 'name' and its
     * role id.
     * 
     * @param roleId the roleId
     * @param rightName the right name
     * @return true, if success
     */
    boolean existWithRoleIdAndRightName(Integer roleId, String rightName);

    /**
     * Search a right with its name and its role primary key.
     * 
     * @param roleId the roleId
     * @param rightName the right name
     * @return the right
     */
    Right findByRoleIdAndRightName(Integer roleId, String rightName);

    /**
     * Count a role numbers of rights.
     * 
     * @param roleId the roleId
     * @return the number of rights corresponding to the role id
     */
    Integer countForRoleId(Integer roleId);

    /**
     * Search all the role's rights.
     * 
     * @param rolePrimaryKey the role primary key
     * @return the list
     */
    List<Right> findAllByRoleId(Integer roleId);
	
}
