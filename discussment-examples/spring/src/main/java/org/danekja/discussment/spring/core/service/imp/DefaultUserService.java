package org.danekja.discussment.spring.core.service.imp;

import org.danekja.discussment.core.accesscontrol.domain.IDiscussionUser;
import org.danekja.discussment.core.accesscontrol.exception.DiscussionUserNotFoundException;
import org.danekja.discussment.spring.core.dao.UserDao;
import org.danekja.discussment.spring.core.domain.User;
import org.danekja.discussment.spring.core.service.UserService;
import org.danekja.discussment.spring.session.UserSession;
import org.danekja.discussment.ui.wicket.session.SessionUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.wicket.ThreadContext.getSession;

/**
 * Created by Martin Bláha on 20.01.17.
 */
@Transactional
public class DefaultUserService implements UserService {

    private UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;

    }

    public User addUser(User entity) {

        return userDao.save(entity);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public IDiscussionUser getUserById(String userId) throws DiscussionUserNotFoundException {
        IDiscussionUser user = userDao.getById(Long.valueOf(userId));
        if(user == null) {
            throw new DiscussionUserNotFoundException(userId);
        }
        return user;
    }

    public User getUserByUsername(String username) {

        return userDao.getUserByUsername(username);
    }

    public IDiscussionUser getCurrentlyLoggedUser() {
        UserSession userSession = (UserSession) getSession();
        return getUserByUsername(userSession.getUser());
    }
}
