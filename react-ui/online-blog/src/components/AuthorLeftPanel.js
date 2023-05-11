import React from "react";
import "./AuthorLeftPanel.css";
import PostList from "./PostList";
import useAuth from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";



const AuthorLeftPanel = ({ author }) => {
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
            {author.firstName + " " + author.lastName}
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
          >{auth?.user?.following.some(usr => usr.id == author.id) ? "Following" : "Follow"}
          </button>
        </span>
      </div>
      <div className="leftPanel__page__header">
        <div className="leftPanel__page__header__text__container">
          <button className="leftPanel__page__header__title" onClick={()=> navigate("/")}>Home</button>
        </div>
      </div>
      <div className="leftPanel__page__articles">
        <PostList
          searchBy={"author"}
          searchString={author.username}
          title={""}
        />
      </div>
    </div>
  );
};

export default AuthorLeftPanel;
