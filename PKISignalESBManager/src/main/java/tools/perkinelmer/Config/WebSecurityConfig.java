package tools.perkinelmer.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tools.perkinelmer.security.CustomUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        return new CustomUserService();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService())
        	.passwordEncoder(passwordEncoder()); //user Details Service验证
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
        		.antMatchers("/account/**","/account**").hasRole("1")
        		.antMatchers("/service/**","/service**").hasRole("3")
        		.antMatchers("/subscription/**","/subscription**").hasRole("5")
        		.antMatchers("/calllog/**","/calllog**").hasRole("7")
        		.antMatchers("/user/**","/user**").hasRole("9")
        		.antMatchers("/role/**","/role**").hasRole("11")
        		.antMatchers("/workflow/**","/workflow**").hasRole("13")
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/main")
                .failureUrl("/login?error")
                .permitAll() //登录页面用户任意访问
                .and()
                .headers().frameOptions().disable()
                .and()
                .logout().permitAll(); //注销行为任意访问
    }
    /**
     * 密码加密
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/**/*.js",
                "/**/*.css",
                "/**/*.jpg",
                "/**/*.png");
    }
}
