import React, { useState } from "react";
import { useParams } from "react-router-dom";
import PostContent from "./PostContent";
import { useEffect } from "react";
import "./PostReader.css";
import useAuth from "../hooks/useAuth";
import useAxiosPrivate from "../hooks/useAxiosPrivate";

const PostReader = () => {
  const params = useParams();

  const axiosPrivate = useAxiosPrivate();
  const { auth, setAuth } = useAuth();
  const [postObj, setPostObj] = useState({});
  const [author, setAuthor] = useState([]);
  const [hasVoted, setHasVoted] = useState(0);
  const [hasCommented, setHasCommented] = useState(false);

  const [errMsg, setErrMsg] = useState("");
  const [refreshCount, setRefreshCount] = useState(0);

  const POSTS_URL = "/post";

  const USER_URL = "/userprofile";

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const postsResponse = await axiosPrivate.get(
          POSTS_URL + "/" + params.postId
        );
        setPostObj(postsResponse.data);
        const authorName = postsResponse.data.createdBy;

        const authorResponse = await axiosPrivate.get(
          USER_URL + "/" + authorName
        );
        setAuthor(authorResponse.data);
      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg("Missing Username or Password");
        } else if (err.response?.status === 401) {
          setErrMsg(
            "Unauthorized. Login again by clicking on sign-in on top right"
          );
          setAuth(null);
        } else {
          setErrMsg("Posts could not be fetched");
        }
      }
    };
    fetchPost();
  }, [params]);

  useEffect(() => {
    const isUpvoted = async () => {
      try {
        const resp = await axiosPrivate.get(
          POSTS_URL + "/" + params.postId + "/hasvoted"
        );
        setHasVoted(resp.data);
      } catch (err) {
        setErrMsg("Posts could not be fetched");
      }
    };
    const isCommented = async () => {
      try {
        const resp = await axiosPrivate.get(
          POSTS_URL + "/" + params.postId + "/comment/iscommented"
        );
        setHasCommented(resp.data);
      } catch (err) {
        setErrMsg("Posts could not be fetched");
      }
    };
    isUpvoted();
    isCommented();
    refreshPost();
  }, []);

  const refreshPost = () => {
    console.log("Post Refreshed");
    setRefreshCount((refreshCount) => refreshCount + 1);
  };
  const addComment = (newcomment) => {
    let commentURL = POSTS_URL + "/" + params.postId + "/comment";
    axiosPrivate
      .post(commentURL, newcomment)
      .then((res) => {
        setPostObj({
          ...postObj,
          comments: [...postObj.comments, res.data],
          commentCount: postObj.commentCount + 1,
        });
      })
      .catch((err) => console.log(err));
    refreshPost();
  };

  const UpVoteAPost = () => {
    const updateVoteCount = (vote) => {
        if(vote=="upVote") {
            if(postObj.hasVoted == -1) {
                return postObj.upVoteCount + 1;
            }else if(postObj.hasVoted == 1) {
                return postObj.upVoteCount -1;
            } else {
                return postObj.upVoteCount + 1
            }
        }
        if(vote=="downVote") {
                if(postObj.hasVoted == -1) {
                    return postObj.downVoteCount -1;
                }else if(postObj.hasVoted == 1) {
                    return postObj.downVoteCount ;
                } else {
                    return postObj.downVoteCount
            }
        }
    }
    let UPVOTEURL = POSTS_URL + "/" + params.postId + "/upvote";
    axiosPrivate
      .post(UPVOTEURL)
      .then((res) => {
        setPostObj({
          ...postObj,
          upVoteCount: updateVoteCount("upVote"),
          downVoteCount: updateVoteCount("downVote"),
          hasVoted: postObj.hasVoted == 1 ? 0 : -1 ? 1 : 1,
        });
      })
      .catch((err) => console.log(err));
    refreshPost();
  };

  const DownVoteAPost = () => {
    const updateVoteCount = (vote) => {
        if(vote=="upVote") {
            if(postObj.hasVoted == -1) {
                return postObj.upVoteCount;
            }else if(postObj.hasVoted == 1) {
                return postObj.upVoteCount -1;
            } else {
                return postObj.upVoteCount 
            }
        }
        if(vote=="downVote") {
                if(postObj.hasVoted == -1) {
                    return postObj.downVoteCount -1;
                }else if(postObj.hasVoted == 1) {
                    return postObj.downVoteCount + 1;
                } else {
                    return postObj.downVoteCount + 1
            }
        }
    }
       
    let url = POSTS_URL + "/" + params.postId + "/downvote";
    axiosPrivate
      .post(url)
      .then((res) => {
        setPostObj({
          ...postObj,
          upVoteCount: updateVoteCount("upVote"),
          downVoteCount: updateVoteCount("downVote"),
          hasVoted: postObj.hasVoted == -1 ? 0 : 1 ? -1 : -1,
        });
      })
      .catch((err) => console.log(err));
    refreshPost();
  };


  const HandleFollow = () => {
    const followReq = async () => {
      try {
        const following = auth?.user?.following.some(
          (usr) => usr.username == author.username
        )
          ? "unfollow"
          : "follow";

        const payload = {
          userId: auth.user.id,
          toFollowId: author.id,
          toUnFollowId: author.id,
        };
        const response = await axiosPrivate.post(
          USER_URL + `/${following}`,
          payload
        );

        const preuser = auth.user;
        following == "follow"
          ? preuser.following.push(author)
          : preuser.following.pop(author);
        setAuth({
          ...auth,
          user: { ...auth.user, following: [...preuser.following] },
        });
        localStorage.setItem("user-info", JSON.stringify(auth));
      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg("Bad Request ");
        } else if (err.response?.status === 401) {
          setErrMsg(
            "Unauthorized. Login again by clicking on sign-in on top right"
          );
          setAuth(null);
        } else {
          setErrMsg("Posts could not be fetched");
        }
      }
    };
    followReq();
  };

  return (
    <div className="postContainer">
      <PostContent
        postObj={postObj}
        userObj={author}
        addCommentEvt={addComment}
        upVoteEVT= {UpVoteAPost}
        downVoteEVT= {DownVoteAPost}
        postDetailsObj={postObj.postDetails}
        refreshPost={refreshPost}
        hasVoted={hasVoted}
        hasCommented={hasCommented} 
      />
    </div>
  );
};

export default PostReader;
