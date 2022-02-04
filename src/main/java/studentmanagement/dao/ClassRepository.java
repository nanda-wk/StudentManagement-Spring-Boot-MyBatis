package studentmanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import studentmanagement.dto.ClassRequestDTO;
import studentmanagement.dto.ClassResponseDTO;

@Mapper
public interface ClassRepository {

	String insert = "INSERT INTO class (id, name) VALUES (#{id}, #{name})";
	
	String selectAll = "SELECT * FROM class";
	
	String selectFilter = "SELECT * FROM class WHERE id=#{id} OR name=#{name}";
	
	@Insert(insert)
	public int insertClass(ClassRequestDTO dto);
	
	@Select(selectAll)
	public List<ClassResponseDTO> selectClass();
	
	@Select(selectFilter)
	public List<ClassResponseDTO> selectone(ClassRequestDTO dto);
}
