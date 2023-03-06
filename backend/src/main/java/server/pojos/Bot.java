package server.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer uid;
    private String name;
    private String description;
    private String code;
    private Integer rating;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date edittime;

    public static final Integer MAX_NAME_LENGTH = 100;
    public static final Integer MAX_DESCRIPTION_LENGTH = 500;
    public static final Integer MAX_CODE_LENGTH = 10000;
}
