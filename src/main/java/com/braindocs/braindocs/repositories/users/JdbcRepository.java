package com.braindocs.braindocs.repositories.users;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import com.braindocs.braindocs.models.users.UserModel;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JdbcRepository {
    private static final String USER_ID = "users.id";
    private static final String USER_EMAIL = "users.email";
    private static final String USER_PASSWORD = "users.password";
    private static final String ROLE_ID = "users_roles.user_id";
    private static final String ROLE_NAME = "users_roles.role_id";
    private final NamedParameterJdbcTemplate template;
    private final String getQuery;

    JdbcRepository(DataSource dataSource, ResourceLoader resourceLoader) {
        template = new NamedParameterJdbcTemplate(dataSource);
        getQuery = readAsString(resourceLoader.getResource("classpath:sql/get.sql"));
    }

    public Optional<UserModel> getByEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put(USER_EMAIL, email);
        try {
            return Optional.ofNullable(template.queryForObject(
                    getQuery,
                    params,
                    getMapper()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<UserModel> getMapper() {
        return (rs, rowNum) -> {
            UserModel user = new UserModel();
            user.setId(rs.getLong(USER_ID));
            user.setEmail(rs.getString(USER_EMAIL));
            user.setPassword(rs.getString(USER_PASSWORD));

            return user;
        };
    }

    public static String readAsString(Resource resource) {
        try {
            InputStream is = resource.getInputStream();
            String var2;
            try {
                var2 = readAsString(is);
            } catch (Throwable var5) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (is != null) {
                is.close();
            }
            return var2;
        } catch (IOException var6) {
            throw new IllegalStateException("Can't read file from resource " + resource, var6);
        }
    }

    public static String readAsString(InputStream inputStream) {
        try {
            return StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        } catch (IOException var2) {
            throw new IllegalStateException("Can't read input stream", var2);
        }
    }
}