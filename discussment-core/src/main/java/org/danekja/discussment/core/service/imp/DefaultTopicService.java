package org.danekja.discussment.core.service.imp;

import org.danekja.discussment.core.accesscontrol.domain.AccessDeniedException;
import org.danekja.discussment.core.accesscontrol.domain.Action;
import org.danekja.discussment.core.accesscontrol.domain.IDiscussionUser;
import org.danekja.discussment.core.accesscontrol.domain.PermissionType;
import org.danekja.discussment.core.accesscontrol.service.AccessControlService;
import org.danekja.discussment.core.accesscontrol.service.DiscussionUserService;
import org.danekja.discussment.core.dao.TopicDao;
import org.danekja.discussment.core.domain.Category;
import org.danekja.discussment.core.domain.Topic;
import org.danekja.discussment.core.service.TopicService;

import java.util.List;

public class DefaultTopicService implements TopicService {

    private TopicDao topicDao;
    private AccessControlService accessControlService;
    private DiscussionUserService discussionUserService;

    public DefaultTopicService(TopicDao topicDao, AccessControlService accessControlService, DiscussionUserService discussionUserService) {
        this.topicDao = topicDao;
        this.accessControlService = accessControlService;
        this.discussionUserService = discussionUserService;
    }

    @Override
    public Topic createTopic(Category category, Topic topic) throws AccessDeniedException {
        if (accessControlService.canAddTopic(category)) {
            topic.setCategory(category);
            return topicDao.save(topic);
        } else {
            throw new AccessDeniedException(Action.CREATE, getCurrentUserId(), category.getId(), PermissionType.TOPIC);
        }
    }

    @Override
    public Topic getTopicById(long topicId) throws AccessDeniedException {
        Topic t = topicDao.getById(topicId);
        if(t == null) {
            return null;
        }

        if (accessControlService.canViewTopics(t.getCategory())) {
            return t;
        } else {
            throw new AccessDeniedException(Action.VIEW, getCurrentUserId(), topicId, PermissionType.TOPIC);
        }
    }

    @Override
    public List<Topic> getTopicsByCategory(Category category) throws AccessDeniedException {

        if (accessControlService.canViewTopics(category)) {
            return topicDao.getTopicsByCategory(category);
        } else {
            throw new AccessDeniedException(Action.VIEW, getCurrentUserId(), category.getId(), PermissionType.TOPIC);
        }
    }

    @Override
    public List<Topic> getTopicsWithoutCategory() {
        // todo: [#24] default no-category
        return null;
    }

    @Override
    public void removeTopic(Topic topic) throws AccessDeniedException {
        if (accessControlService.canRemoveTopic(topic)) {
            topicDao.remove(topic);
        } else {
            throw new AccessDeniedException(Action.DELETE, getCurrentUserId(), topic.getId(), PermissionType.TOPIC);
        }
    }

    /**
     * Returns the id of the currently logged user. Used when throwing access denied exception.
     * @return Id of the currently logged user or null if no user is logged.
     */
    private String getCurrentUserId() {
        IDiscussionUser user = discussionUserService.getCurrentlyLoggedUser();
        return user == null ? null : user.getDiscussionUserId();
    }
}
