package com.ness.postservice.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "comment_votes")
public class CommentVotes extends Base {

	private static final long serialVersionUID = 1L;

	private int score; //-1 downvote +1 for upvote


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotNull
	@JsonIgnore
	private Comment comment;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "CommentVotes [score=" + score + ", createdBy=" + createdBy + ", comment=" + comment + ", id=" + id
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

	
}