package com.ness.postservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "post_comment")
public class Comment extends Base {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	@NotNull
	@JsonIgnore
	private Post post;

	private String review;
	
	@Column(name = "up_vote_count", nullable = false)
	protected int upVoteCount = 0;

	@Column(name = "down_vote_count", nullable = false)
	protected int downVoteCount = 0;

	public Post getPost() {
		return post;
	}

	public Comment setPost(Post post) {
		this.post = post;
		return this;
	}

	public String getReview() {
		return review;
	}

	public Comment setReview(String review) {
		this.review = review;
		return this;
	}

	public int getUpVoteCount() {
		return upVoteCount;
	}

	public int getDownVoteCount() {
		return downVoteCount;
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

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Comment)) {
			return false;
		}
		Comment other = (Comment) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Comment [post=" + post + ", review=" + review + ", upVoteCount=" + upVoteCount + ", downVoteCount="
				+ downVoteCount + ", id=" + id +  ", createdBy=" + createdBy + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}



}
