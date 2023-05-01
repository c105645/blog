import React, { useState, useEffect } from "react";
import PostTile from "./PostTile";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import {Alert} from "reactstrap";
import useAuth from "../hooks/useAuth";

const PostList = (props) => {
  const [postArr, setPostArr] = useState([]);
  const [errMsg, setErrMsg] = useState('');
  const axiosPrivate = useAxiosPrivate();
  const { setAuth } = useAuth();

  const POSTLIST_URL = "/post";

  useEffect(() => {
    const fetchposts = async () => {
      try {
        const response = await axiosPrivate.get(
          POSTLIST_URL + "?searchby=" + props.searchBy + "&page=0&size=20",
        );

        setPostArr(response.data);
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
    fetchposts();

  }, []);


  return (
    errMsg.length > 0 ? (<Alert color="danger">
    {errMsg}
  </Alert>) : (

    <div className="tileList">
      {postArr.map((post) => (
        <div key= {post.id} className="tile">
          <PostTile post= {post} showReason = {props.title} />
        </div>
      ))}
    </div>
  )
  );
};

export default PostList;
