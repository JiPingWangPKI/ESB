package tools.perkinelmer.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tools.perkinelmer.Mapper.UserMapper;
import tools.perkinelmer.entity.SysAuthority;
import tools.perkinelmer.entity.SysRole;
import tools.perkinelmer.entity.SysUser;

/**
 * 用于将用户权限交给 springsecurity 进行管控
 * @author wangj01052
 *
 */
public class CustomUserService implements UserDetailsService{
	@Autowired
	private UserMapper userMapper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user = userMapper.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
        for(SysRole role:user.getRoles())
        { 
        	List<SysAuthority> authoritys = role.getAuthoritys();
        	for(SysAuthority authority:authoritys){
        		authorities.add(new SimpleGrantedAuthority("ROLE_"+authority.getAuthority_id().toString()));
        	}
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
	}
}
