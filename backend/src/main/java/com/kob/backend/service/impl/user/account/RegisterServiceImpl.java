package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "Username cannot be empty!");
            return map;
        }
        if (password == null || confirmedPassword == null) {
            map.put("error_message", "Password cannot be empty!");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "Username cannot be empty!");
            return map;
        }

        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "Password cannot be empty!");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "Username cannot be longer than 100!");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "Password cannot be longer than 100!");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "Confirmed password is different!");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "Username already exists!");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://ui-avatars.com/api/?name=" + username;
        User user = new User(null, username, encodedPassword, photo, 1500);
        userMapper.insert(user);
        String defaultCode = "package com.kob.botrunningsystem.utils;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.io.FileNotFoundException;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Scanner;\n" +
                "\n" +
                "public class Bot implements java.util.function.Supplier<Integer> {\n" +
                "    static class Cell {\n" +
                "        public int x, y;\n" +
                "        public Cell(int x, int y) {\n" +
                "            this.x = x;\n" +
                "            this.y = y;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private boolean check_tail_increasing(int step) {\n" +
                "        if (step <= 10) return true;\n" +
                "        return step % 3 == 1;\n" +
                "    }\n" +
                "\n" +
                "    public List<Cell> getCells(int sx, int sy, String steps) {\n" +
                "        steps = steps.substring(1, steps.length() - 1);\n" +
                "        List<Cell> res = new ArrayList<>();\n" +
                "\n" +
                "        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\n" +
                "        int x = sx, y = sy;\n" +
                "        int step = 0;\n" +
                "        res.add(new Cell(x, y));\n" +
                "        for (int i = 0; i < steps.length(); i ++ ) {\n" +
                "            int d = steps.charAt(i) - '0';\n" +
                "            x += dx[d];\n" +
                "            y += dy[d];\n" +
                "            res.add(new Cell(x, y));\n" +
                "            if (!check_tail_increasing( ++ step)) {\n" +
                "                res.remove(0);\n" +
                "            }\n" +
                "        }\n" +
                "        return res;\n" +
                "    }\n" +
                "\n" +
                "    public Integer nextMove(String input) {\n" +
                "        String[] strs = input.split(\"#\");\n" +
                "        int[][] g = new int[13][14];\n" +
                "        for (int i = 0, k = 0; i < 13; i ++ ) {\n" +
                "            for (int j = 0; j < 14; j ++, k ++ ) {\n" +
                "                if (strs[0].charAt(k) == '1') {\n" +
                "                    g[i][j] = 1;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);\n" +
                "        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);\n" +
                "\n" +
                "        List<Cell> aCells = getCells(aSx, aSy, strs[3]);\n" +
                "        List<Cell> bCells = getCells(bSx, bSy, strs[6]);\n" +
                "\n" +
                "        for (Cell c: aCells) g[c.x][c.y] = 1;\n" +
                "        for (Cell c: bCells) g[c.x][c.y] = 1;\n" +
                "\n" +
                "        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\n" +
                "        for (int i = 0; i < 4; i ++ ) {\n" +
                "            int x = aCells.get(aCells.size() - 1).x + dx[i];\n" +
                "            int y = aCells.get(aCells.size() - 1).y + dy[i];\n" +
                "            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {\n" +
                "                return i;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return 0;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Integer get() {\n" +
                "        File file = new File(\"input.txt\");\n" +
                "        try {\n" +
                "            Scanner sc = new Scanner(file);\n" +
                "            return nextMove(sc.next());\n" +
                "        } catch (FileNotFoundException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), "Default bot", "", defaultCode, now, now);

        botMapper.insert(bot);

        map.put("error_message", "success");
        return map;
    }
}
