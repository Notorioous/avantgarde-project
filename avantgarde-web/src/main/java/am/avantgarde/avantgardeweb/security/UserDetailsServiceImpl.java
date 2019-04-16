package am.avantgarde.avantgardeweb.security;

import am.avantgarde.avantgardecommon.model.User;
import am.avantgarde.avantgardecommon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User byEmail = userRepository.findByEmail(email);
        if(byEmail == null){
            throw new UsernameNotFoundException("user with " + email + " does not exist");
        }

        return new SpringUser(byEmail);
    }
}
