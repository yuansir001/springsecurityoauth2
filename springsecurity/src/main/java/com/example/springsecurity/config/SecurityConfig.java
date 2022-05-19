package com.example.springsecurity.config;

import com.example.springsecurity.handler.MyAccessDeniedHandler;
import com.example.springsecurity.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * security配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    DataSource dataSource;
    @Autowired
    PersistentTokenRepository persistentTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交
        http.formLogin()
                .usernameParameter("userName")
                .passwordParameter("passWord")
                //当发现/login时认为是登陆，必须和表单提交的地址一致，去执行UserDetailsServiceImpl
                .loginProcessingUrl("/login")
                // 自定义登陆页面
                .loginPage("/login.html")
                // 登陆成功后跳转页面为post请求
                .successForwardUrl("/toMain")
                // 登陆成功后处理器不能和successForwardUrl共存
                //.successHandler(new MyAuthenticationSuccessHandler("http://www.baidu.com"))
                // 登陆失败页面跳转，post请求
                .failureForwardUrl("/toError");
                // 登陆失败后处理器不能和ailureForwardUrl共存
                //.failureHandler(new MyAuthenticationFailureHandler("/error.html"));

        // 授权认证
        http.authorizeRequests()
                // error.htm不需要认证
                .antMatchers("/error.html").permitAll()
                // login.html不需要被认证
                .antMatchers("/login.html").permitAll()
                // 静态资源不拦截(*：匹配1或多个字符，**：匹配1或多个目录)
                .antMatchers("/js/**", "/css/**", "/images/**").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                // 正则校验（.:任意，+：至少一个，[.]转译.）
                //.regexMatchers(".+[.]jpg").permitAll()
                //.regexMatchers(HttpMethod.POST, "/demo").permitAll()
                // 访问角色权限
                //.antMatchers("/main1.html").hasAuthority("admin1")
                //.antMatchers("/main1.html").hasAnyAuthority("admin", "admin1")
                // 角色权限
                //.antMatchers("/main1.html").hasRole("abc")
                //.antMatchers("/main1.html").hasAnyRole("abC,abc")
                // ip判断权限
                //.antMatchers("/main1.html").hasIpAddress("127.0.0.1")
                // 所有请求页面都要认证
                .anyRequest().authenticated();
                //.anyRequest().access("@myServiceImpl.hasPermission(request, authentication)");

        // 关闭csrf防护
        http.csrf().disable();

        // 异常处理
        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        // 记住我
        http.rememberMe()
                // 失效时间
                .tokenValiditySeconds(60)
                // 变量名自定义
                //.rememberMeParameter("rememberMe")
                // 自定义登陆逻辑
                .userDetailsService(userDetailsService)
                // 持久层对象
                .tokenRepository(persistentTokenRepository);

        // 退出登陆
        http.logout()
                // 退出登录跳转页面
                .logoutSuccessUrl("/login.html");
    }

    @Bean
    public PasswordEncoder getPw(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次启动自动建表，第二次要注释掉
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
