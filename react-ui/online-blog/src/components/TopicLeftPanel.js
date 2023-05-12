import React from "react";
import "./AuthorLeftPanel.css";
import PostList from "./PostList";
import useAuth from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";



const TopicLeftPanel = ({ topic, followEvt }) => {
  const { auth } = useAuth();

  const navigate = useNavigate();


  return (
    <div className="leftPanel">
      <div className="leftPanel__header">
        <span>
          <h1
            style={{
              fontWeight: "700",
              fontSize: "42px",
              color: "black",
              marginTop: "10px",
            }}
          >
            {topic.name }
          </h1>
        </span>
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
      </div>
      <div className="leftPanel__page__header">
        <div className="leftPanel__page__header__text__container">
          <button className="leftPanel__page__header__title" onClick={()=> navigate("/")}>Home</button>
        </div>
      </div>
      <div>
        <PostList
          searchBy={"categoery"}
          searchString={topic.name}
          title={""}
        />
      </div>
    </div>
  );
};

export default TopicLeftPanel;
