import React, { useState, useEffect } from "react";
import "./SearchComponent.css";
import PostList from "./PostList";
import { useLocation, useNavigate } from "react-router-dom";

const SearchComponent = ({}) => {
    const location = useLocation();
    const navigate = useNavigate();
    const [searchString, setSearchString] = useState(location.state.inputstring);


  return (
    <div className="main-container">
      <div className="left-container">
        <h2>
          <span style={{ color: "grey" }}>Results for </span>&nbsp;
          {searchString}
        </h2>
        <div className="left-container-Menu">
            <button  style={{backgroundColor: "white", outeline: "none", border: "none"}} onClick={()=> navigate("/")}>Home</button>
        </div>

        <div>
          <PostList
            searchBy={"categoery"}
            searchString={"Spring Boot"}
            title={""}
          />
        </div>
      </div>
      <div className="right-container">
        <div className="right-top-container"></div>
        <div className="right-bottom-container"></div>
      </div>
    </div>
  );
};
export default SearchComponent;
