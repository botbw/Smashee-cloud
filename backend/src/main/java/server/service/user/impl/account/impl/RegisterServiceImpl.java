package server.service.user.impl.account.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kotlin.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.mappers.UserMapper;
import server.pojos.User;
import server.service.user.impl.account.RegisterService;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    private UserMapper userMapper;
    private ApplicationContext context;


    RegisterServiceImpl(@Autowired UserMapper userMapper, @Autowired ApplicationContext context) {
        this.userMapper = userMapper;
        this.context = context;
    }

    @Override
    public Map<String, String> register(String usrname, String pwd, String confirmedPwd) {
        Map<String, String> ret = new HashMap<>();

        Pair<Boolean, String> usrnameCheck = isUsrnameValid(usrname);
        if (!usrnameCheck.getFirst()) {
            ret.put("MSG", usrnameCheck.getSecond());
            return ret;
        }

        Pair<Boolean, String> pwdCheck = isPasswordValid(pwd, confirmedPwd);
        if(!pwdCheck.getFirst()) {
            ret.put("MSG", pwdCheck.getSecond());
            return ret;
        }
        addUser(usrname, pwd);
        ret.put("MSG", "succeed");
        return ret;
    }

    private Pair<Boolean, String> isUsrnameValid(String usrname) {
        if (usrname == null || usrname.length() == 0) {
            return new Pair<>(false, "usrname cannot be empty");
        }
        String usrnameWithoutSpace = usrname.replaceAll("\\s*", "");
        if (usrnameWithoutSpace.length() != usrname.length()) {
            return new Pair<>(false, "usrname cannot contain space");
        }
        if (usrname.length() > User.MAX_USRNAME_LENGTH) {
            return new Pair<>(false, "username cannot be longer than " + User.MAX_USRNAME_LENGTH);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("usrname", usrname);

        if (userMapper.selectOne(queryWrapper) != null) {
            return new Pair<>(false, "username already exists");
        }
        return new Pair<>(true, "username is valid");
    }

    private Pair<Boolean, String> isPasswordValid(String pwd, String confirmedPwd) {
        if (pwd == null || pwd.length() == 0) {
            return new Pair<>(false, "password cannot be empty");
        }
        if (pwd.length() > User.MAX_PWD_LENGTH) {
            return new Pair<>(false, "password cannot be longer than " + User.MAX_USRNAME_LENGTH);
        }
        if (!pwd.equals(confirmedPwd)) {
            return new Pair<>(false, "confirmed password not same");
        }
        return new Pair<>(true, "password is valid");
    }

    public int addUser(String userName, String password) {
        PasswordEncoder passwordEncoder = (PasswordEncoder) context.getBean(PasswordEncoder.class);
        String encodedPwd = passwordEncoder.encode(password);
        User user = new User(null, userName, encodedPwd, null);
        return userMapper.insert(user);
    }
}
