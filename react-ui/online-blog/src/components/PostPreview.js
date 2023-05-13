import React, { useEffect, useState } from "react";

import parse, { domToReact } from "html-react-parser";

import "./PostPreview.css";

const PostPreview = (props) => {
  return (
    <div>
      <div className="postHeader">
        <img className="profilePhoto" src={props.userObj.imageUrl}></img>
        <div>
          <div style={{ fontSize: "1.4rem" }}>
            {props.userObj.firstName + " " + props.userObj.lastName}
          </div>
          <div style={{ fontSize: "1rem", color: "#7d848b" }}>
            {props.postObj.updatedAt}
          </div>
        </div>
      </div>
      <div className="postContent">
        <div
          style={{
            fontSize: "2.2rem",
            fontWeight: "bold",
            lineHeight: "2.5rem",
            paddingBottom: "1rem",
          }}
        >
          {props.postObj.title}
        </div>
        {props.postDetailsObj
          ? props.postDetailsObj.content
            ? parse(props.postDetailsObj.content)
            : null
          : null}
      </div>
    </div>
  );
};

export default PostPreview;
