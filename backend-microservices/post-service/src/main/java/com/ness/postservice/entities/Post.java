package com.ness.postservice.entities;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post extends Base {

	private static final long serialVersionUID = 1L;

	private String title;

	private String short_description;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.MERGE })
	@JsonManagedReference
	private List<Comment> comments = new ArrayList<>();
	
	@OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	@JsonManagedReference
	private PostDetails postDetails;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@JsonManagedReference
	private List<Tag> tags = new ArrayList<>();

	private String category;


	@Column(name = "comment_count", nullable = false)
	private int commentCount = 0;

	@Column(name = "up_vote_count", nullable = false)
	private int upVoteCount = 0;

	@Column(name = "down_vote_count", nullable = false)
	private int downVoteCount = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public PostDetails getPostDetails() {
		return postDetails;
	}


	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getUpVoteCount() {
		return upVoteCount;
	}

	public void setUpVoteCount(int upVoteCount) {
		this.upVoteCount = upVoteCount;
	}

	public int getDownVoteCount() {
		return downVoteCount;
	}

	public void setDownVoteCount(int downVoteCount) {
		this.downVoteCount = downVoteCount;
	}

	public Post setPostDetails(PostDetails postDetails) {
		if (postDetails == null) {
			if (this.postDetails != null) {
				this.postDetails.setPost(null);
			}
		} else {
			postDetails.setPost(this);
		}
		this.postDetails = postDetails;
		return this;
	}

	public Post addComment(Comment comment) {
		comments.add(comment);
		comment.setPost(this);
		return this;
	}

	public Post removeComment(Comment comment) {
		comments.remove(comment);
		comment.setPost(null);
		return this;
	}

	public void increaseUpVoteCount() {
		upVoteCount = upVoteCount + 1;
	}

	public void increaseDownVoteCount() {
		downVoteCount = downVoteCount + 1;
	}

	public void decreaseUpVoteCount() {
		if (upVoteCount > 0) {
			upVoteCount = upVoteCount - 1;
		}
	}

	public void decreaseDownVoteCount() {
		if (downVoteCount > 0) {
			downVoteCount = downVoteCount - 1;
		}
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	

	public Post addTag(Tag tag) {
		tags.add(tag);
		return this;
	}
	
	public Post removeTag(Tag tag) {
		tags.remove(tag);
		return this;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Post)) {
			return false;
		}
		Post other = (Post) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Post [title=" + title + ", short_description=" + short_description + ", " 
				+ ", postDetails=" + postDetails + ", tags=" + tags + ", category=" + category + ", createdBy=" + createdBy
				+ ", commentCount=" + commentCount + ", upVoteCount=" + upVoteCount + ", downVoteCount=" + downVoteCount
				+ ", id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

	public void setCreatedBy(String user) {
		this.createdBy = user;
		
	}


}
