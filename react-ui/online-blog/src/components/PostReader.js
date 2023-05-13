import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import PostContent from './PostContent';
import { useEffect } from 'react';
import './PostReader.css';
import useAuth from '../hooks/useAuth';
import useAxiosPrivate from '../hooks/useAxiosPrivate';

const PostReader = () => {
    const params = useParams();
    
    const axiosPrivate = useAxiosPrivate();
    const { auth, setAuth } = useAuth();
    const [postObj, setPostObj] = useState([]);
    const [author, setAuthor] = useState([]);
    const [errMsg, setErrMsg] = useState("");
    const [refreshCount, setRefreshCount] = useState(0);
    const POSTS_URL = "/post";
    
    const USER_URL = "/userprofile";

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const postsResponse = await axiosPrivate.get(
                    POSTS_URL+"/" + params.postId
                );
                setPostObj(postsResponse.data);
                const authorName = postsResponse.data.createdBy;
                
                const authorResponse = await axiosPrivate.get(
                    USER_URL + "/"+authorName
                );
                setAuthor(authorResponse.data);
            } catch (err) {
              if (!err?.response) {
                setErrMsg("No Server Response");
              } else if (err.response?.status === 400) {
                setErrMsg("Missing Username or Password");
              } else if (err.response?.status === 401) {
                setErrMsg("Unauthorized. Login again by clicking on sign-in on top right");
                setAuth(null);
              } else {
                setErrMsg("Posts could not be fetched");
              }
      
            }
      
        };
        fetchPost();
    }, [params]);

    const refreshPost = ()=> {
        console.log("Post Refreshed");
        setRefreshCount((refreshCount) => refreshCount + 1);
    }
    
    const HandleFollow = () => {
        const followReq = async () => {
        try {
            const following = auth?.user?.following.some(usr => usr.username == author.username) ? "unfollow" : "follow";

        const payload =  {
            "userId": auth.user.id,
            "toFollowId": author.id,
            "toUnFollowId": author.id

        }
            const response = await axiosPrivate.post(
                USER_URL + `/${following}`, payload);

            const preuser = auth.user;
            following == "follow" ? preuser.following.push(author) : preuser.following.pop(author);
            setAuth({...auth, user: {...auth.user, following: [...preuser.following]}});
            localStorage.setItem("user-info", JSON.stringify(auth))

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
    }

    return(
        <div className="postContainer">
            <PostContent postObj = {postObj} userObj = {author} postDetailsObj = {postObj.postDetails} refreshPost={refreshPost}/>
        </div>
    );
}

export default PostReader;