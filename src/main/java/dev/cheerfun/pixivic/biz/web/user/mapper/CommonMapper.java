package dev.cheerfun.pixivic.biz.web.user.mapper;

import dev.cheerfun.pixivic.biz.web.common.po.User;
import dev.cheerfun.pixivic.common.util.json.JsonTypeHandler;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface CommonMapper {
    @Select({
            "SELECT IFNULL((\n" +
                    "SELECT 1\n" +
                    "FROM users\n" +
                    "WHERE email=#{email} OR username=#{username}\n" +
                    "LIMIT 1),0)",
    })
    int checkUserNameAndEmail(String username, String email);

    @Insert("insert into users (email, username,password,permission_level,is_ban,star) values (#{email}, #{username}, #{password}, #{permissionLevel}, #{isBan},#{star})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "user_id")
    int insertUser(User user);

    @Select({
            " SELECT * FROM (SELECT * FROM users WHERE username= #{username} OR email=#{username})  temp where temp.PASSWORD=#{password}",
    })
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "isBan", column = "is_ban"),
            @Result(property = "permissionLevel", column = "permission_level"),
            @Result(property = "pixivAccount", column = "pixiv_account"),
            @Result(property = "pixivPassword", column = "pixiv_password"),
            @Result(property = "qqOpenId", column = "qq_open_id")
    })
    User queryUserByusernameAndPassword(String username, String password);

    @Select({
            "SELECT * FROM users WHERE user_id= #{userId} ",
    })
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "isBan", column = "is_ban"),
            @Result(property = "permissionLevel", column = "permission_level"),
            @Result(property = "pixivAccount", column = "pixiv_account"),
            @Result(property = "pixivPassword", column = "pixiv_password"),
            @Result(property = "qqOpenId", column = "qq_open_id")
    })
    //@Cacheable(value = "user", key = "#userId")
    User queryUserByUserId(int userId);

    @Select("SELECT * FROM users WHERE qq_open_id=#{qqOpenId}\n")
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "isBan", column = "is_ban"),
            @Result(property = "permissionLevel", column = "permission_level"),
            @Result(property = "pixivAccount", column = "pixiv_account"),
            @Result(property = "pixivPassword", column = "pixiv_password"),
            @Result(property = "qqOpenId", column = "qq_open_id")
    })
    User getUserByQQOpenId(String qqOpenId);

    @Update("update users set qq_open_id=#{qqOpenId} where user_id=#{userId}")
    int setQQOpenId(String qqOpenId, int userId);

    @Update("update users set avatar=#{avatar} where user_id=#{userId}")
    int setAvatar(String avatar, int userId);

    @Update("update users set email=#{email} , permission_level=2 where user_id=#{userId}")
    int setEmail(String email, int userId);

    @Update("update users set password=#{password} where email=#{email}")
    int setPasswordByEmail(String password, String email);

    @Update("update users set password=#{password} where user_id=#{userId}")
    int setPasswordById(String password, int userId);
}
