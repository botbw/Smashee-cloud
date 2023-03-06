package server.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import server.pojos.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
