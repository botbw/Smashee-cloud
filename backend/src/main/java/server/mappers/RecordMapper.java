package server.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import server.pojos.Record;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
