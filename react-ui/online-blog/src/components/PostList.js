import React, { useState, useEffect } from "react";
import PostTile from "./PostTile";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { Alert } from "reactstrap";
import useAuth from "../hooks/useAuth";
import { useNavigate } from "react-router";

const PostList = ({searchBy, searchString, title, authors}) => {
  const [postArr, setPostArr] = useState([]);
  const [errMsg, setErrMsg] = useState("");
  const axiosPrivate = useAxiosPrivate();
  const {auth,  setAuth } = useAuth();
  const navigate = useNavigate('');
  const POSTLISTURL = "/post";


  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const following = [...auth?.user?.following.map(usr => usr.username)];
        const response = await axiosPrivate.post(
          POSTLISTURL + "/search?page=0&size=20", {"searchBy": searchBy, "searchString": searchString, following, authors }
        );

        setPostArr(response.data);
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
    fetchPosts();
  }, [searchBy, searchString]);

  return errMsg.length > 0 ? (
    <Alert color="danger">{errMsg}</Alert>
  ) : (
    <div className="tileList">
      {postArr.length ? postArr?.map((post) => (
        <div key={post.id} className="tile" onClick={() => navigate("/read/"+post.id)}>
          <PostTile post={post} showReason={title} />
        </div>
      )): null}
    </div>
  );
};

export default PostList;
