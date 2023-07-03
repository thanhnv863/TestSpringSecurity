package com.poly.thanhnvph20218.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    //Mã hóa pass và kiểm tra tính hợp lệ
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //SecurityFilterChain là giao diện chính để cấu hình chuỗi bộ lọc bảo mật
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll() // tất cả mọi người đều truy cập được
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/users").hasRole("ADMIN")// chỉ vai trò admin mới truy cập được
                ).formLogin(
                        form -> form
                                .loginPage("/login") // chuyển hướng đến trang đăng nhập
                                .loginProcessingUrl("/login") // cấu hình lại đường dẫn để đăng nhập
                                .defaultSuccessUrl("/users") // url mặc định sau khi đăng nhập thành công
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // yeu cầu đăng xuất
                                .permitAll()
                );
        return http.build();
    }

    //phương thức này được sử dụng để cấu hình xác thực toàn cục trong Spring Security.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
