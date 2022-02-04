package studentmanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import studentmanagement.dto.UserRequestDTO;
import studentmanagement.dto.UserResponseDTO;

@Mapper
public interface UserRepository {

	String insert = "INSERT INTO user (id, name, password) VALUES (#{id}, #{name}, #{password})";

	String update = "UPDATE user SET name=#{name}, password=#{password} WHERE id=#{id}";
	
	String delete = "DELETE FROM user WHERE id=#{id}";
	
	String selectAll = "SELECT * FROM user";
	
	String selectFilter = "SELECT * FROM user WHERE id=#{id} OR name=#{name}";
	
	@Select(selectAll)
	public List<UserResponseDTO> userAll();
	
	@Select(selectFilter)
	public List<UserResponseDTO> userOne(UserRequestDTO dto);
	
	@Delete(delete)
	public int userDelete(UserRequestDTO dto);
	
	@Update(update)
	public int userUpdate(UserRequestDTO dto);
	
	@Insert(insert)
	public int userAdd(UserRequestDTO dto);
	
}
