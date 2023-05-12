import React, { useState, useEffect } from "react";
import useAuth from "../hooks/useAuth";
import RecommandedAuthors from "./RecommendedAuthors";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import "./AuthorRightPanel.css";

const TopicRightPanel = ({ topic, followEvt }) => {
    const FOLLOWINGLISTURL = "/userprofile/authors/following"


    const axiosPrivate = useAxiosPrivate();
    const { auth, setAuth } = useAuth();
    const [authors, setAuthors] = useState([]);
    const [errMsg, setErrMsg] = useState("");


  useEffect(() => {
    const fetchAuthors = async () => {
      try {
        const authorrs = auth.user.following.map((user) => (user.username));
        const response = await axiosPrivate.post(
          FOLLOWINGLISTURL + "?page=0&size=20", authorrs
        );
        setAuthors(response.data.filter(author=> !auth.user.following[author] && auth.user.username !== author.username));
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
    fetchAuthors();

  }, []);


  return (
    <div className="rightbar">
      <div className="rightbar__user__details">
        <h2
          style={{
            fontSize: "1rem",
            fontWeight: "510",
            color: "rgb(0,0,0)",
          }}
        >
          {topic.name}
        </h2>
      
        <span>
          <button
            style={{
              outline: "none",
              borderRadius:"40px 40px 40px 40px",
              backgroundColor: "green",
              color:"white"

            }}
            onClick={followEvt}

          >{auth?.user?.categories.some(cat => cat.id == topic.id) ? "Following" : "Follow"}
          </button>
        </span>
        <div
          style={{
            fontSize: "0.875rem",
            fontWeight: "400",
            marginBottom: "20px",
            cursor: "pointer",
            width: "430px",
            color: "rgb(117, 117, 117)",
            padding: "20px 20px 50px 0px",
            borderBottom: "1px solid lightgrey",
          }}
        >
          {topic.description}
        </div>
      </div>
      <h3
          style={{
            margin: "90px 10px 30px 10px",
            fontSize: "25px",
            fontWeight: "500",
          }}
        >
          Authors Following {topic.name}
        </h3>

        <RecommandedAuthors authors={authors}/>
    </div>
  );
};

export default TopicRightPanel;
