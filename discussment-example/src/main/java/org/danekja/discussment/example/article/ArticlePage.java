package org.danekja.discussment.example.article;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.discussment.core.dao.jpa.DiscussionJPA;
import org.danekja.discussment.core.dao.jpa.PostJPA;
import org.danekja.discussment.core.domain.Discussion;
import org.danekja.discussment.core.domain.Post;
import org.danekja.discussment.core.service.IDiscussionService;
import org.danekja.discussment.core.service.IPostService;
import org.danekja.discussment.core.service.imp.DiscussionService;
import org.danekja.discussment.core.service.imp.PostService;
import org.danekja.discussment.example.base.BasePage;
import org.danekja.discussment.ui.wicket.panel.discussion.DiscussionPanel;


/**
 * Homepage
 */
public class ArticlePage extends BasePage {

	private static final long serialVersionUID = 1L;

	private static final long DISCUSSION_ID = 0;

	private IDiscussionService discussionService;
	private IPostService postService;

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public ArticlePage(final PageParameters parameters) {

        this.discussionService = new DiscussionService(new DiscussionJPA());
        this.postService = new PostService(new PostJPA());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Discussion discussion = discussionService.getDiscussionById(DISCUSSION_ID);

        if (discussion == null) {
            discussion = discussionService.createDiscussion(new Discussion("article name"));
        }

        add(new DiscussionPanel("content", new Model<Discussion>(discussion), postService, new Model<Post>()));
    }

    @Override
    public String getTitle() {
        return "Article page";
    }
}