package server.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    @NotNull
    private String usrname;
    @NotNull
    private String pwd;
    private String avatar;
    private Integer rating;

    static public final Integer MAX_USRNAME_LENGTH = 50;
    static public final Integer MAX_PWD_LENGTH = 100;
    static public final Integer MAX_AVATAR_LENGTH = 1000;
}
