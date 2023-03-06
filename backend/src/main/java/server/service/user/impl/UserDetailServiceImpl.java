package server.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import server.mappers.UserMapper;
import server.pojos.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private UserMapper userMapper;

    public UserDetailServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String usrname) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usrname", usrname);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("No such username");
        }
        return new UserDetailsImpl(user);
    }
}
