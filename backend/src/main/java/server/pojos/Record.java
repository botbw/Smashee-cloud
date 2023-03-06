package server.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer s1Id;
    private Integer s2Id;
    private Integer s1StX;
    private Integer s1StY;
    private Integer s2StX;
    private Integer s2StY;
    private String s1DirHis;
    private String s2DirHis;

    private Integer rowCnt;
    private Integer colCnt;
    private String gameMap;

    private String result;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;
}
