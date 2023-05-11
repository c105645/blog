import React from "react";
import "./AuthorRightPanel.css";
import RecommandedAuthors from "./RecommendedAuthors";
import useAuth from "../hooks/useAuth";

const AuthorRightPanel = ({ author }) => {
  const { auth } = useAuth();

  if (!author) author = auth.user;

  return (
    <div className="rightbar">
      <div className="rightbar__avatar__container">
        <img
          alt={author.firstName + " " + author.lastName}
          src={author.imageUrl}
          width="88"
          height="88"
        />
      </div>
      <div className="rightbar__user__details">
        <h2
          style={{
            fontSize: "1rem",
            fontWeight: "510",
            color: "rgb(0,0,0)",
          }}
        >
          {author.firstName + " " + author.lastName}
        </h2>
        <p style={{ color: "rgb(117, 117, 117)", fontSize: "15px" }}>
          {author.followers?.length || 0 + "  followers"}{" "}
        </p>
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
        <div
          style={{
            fontSize: "0.875rem",
            fontWeight: "400",
            marginBottom: "20px",
            cursor: "pointer",
            width: "430px",
            color: "rgb(117, 117, 117)",
            padding: "20px 20px 50px 20px",
            borderBottom: "1px solid lightgrey",
          }}
        >
          {author.biography}
        </div>
      </div>
      {author.following?.length && (
        <div className="rightbar__reading__lists">
          <h2
            style={{
              fontSize: "1rem",
              fontWeight: "510",
              color: "rgb(0,0,0)",
              margin: "8px 8px 20px 8px",
            }}
          >
            Following
          </h2>
          <RecommandedAuthors authors={author.following} />
        </div>
      )}
    </div>
  );
};

export default AuthorRightPanel;
