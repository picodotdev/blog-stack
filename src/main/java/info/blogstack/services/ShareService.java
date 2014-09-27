package info.blogstack.services;

import info.blogstack.entities.Post;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public interface ShareService {

	@Transactional(readOnly = true)
	void share(Collection<Post> posts);
}