//package server.controllers.user;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import server.mappers.UserMapper;
//import server.pojos.User;
//
//import java.util.List;
//
//@RestController
//@Component
//@RequestMapping("/user")
//public class UserController {
//    private UserMapper userMapper;
//
//    private ApplicationContext context;
//
//    public UserController(@Autowired UserMapper userMapper, @Autowired ApplicationContext context) {
//        this.userMapper = userMapper;
//        this.context = context;
//    }
//
//    @RequestMapping("getAll")
//    public List<User> getAll() {
//        return userMapper.selectList(null);
//    }
//
//    @RequestMapping("getUser/{userId}")
//    public User getUser(@PathVariable int userId) {
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("uid", userId);
//        return userMapper.selectOne(wrapper);
//    }
//
//    @RequestMapping("addUser/{userId}/{userName}/{password}")
//    public int addUser(
//            @PathVariable Integer userId,
//            @PathVariable String userName,
//            @PathVariable String password) {
//        PasswordEncoder passwordEncoder = (PasswordEncoder) context.getBean(PasswordEncoder.class);
//        String encoded = passwordEncoder.encode(password);
//        User user = new User(userId, userName, encoded, null);
//        return userMapper.insert(user);
//    }
//
//    @RequestMapping("deleteUser/{userId}")
//    public int deleteUser(@PathVariable int userId) {
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("uid", userId);
//        return userMapper.delete(wrapper);
//    }
//}
