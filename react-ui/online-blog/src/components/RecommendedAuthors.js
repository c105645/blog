import React from "react";
import avatar from '../svg/avatar-male.svg';
import './RecommendedAuthors.css';
import { useNavigate } from "react-router-dom";



const RecommendedAuthors = ({authors}) => {


  const navigate = useNavigate();




  return (
    authors?.map((author) => (
    <div key={author.id} className="follow-content">
      <div style={{paddingRight:'10px'}}><img style={{borderRadius: '50%'}} src={avatar} width="25px" height="25px"/> </div>
      <div className="info">
        <span style={{"fontSize": "25px", "fontWeight": "500"}}>{author.firstName} &nbsp; </span>
        <span>@{String(author?.email).split("@")[0]}</span>
        <p>{author.biography.slice(0,80)}...</p>

      </div>
      <button
        style={{
          marginRight: "20px" ,
        }}
        onClick={() => navigate("/author", {state: {type: "author", data: author}})}
      >
        More...
      </button>
    </div>
  )))
};

export default RecommendedAuthors;
