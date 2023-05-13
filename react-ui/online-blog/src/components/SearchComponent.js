import React, { useState, useEffect } from "react";
import "./SearchComponent.css";
import PostList from "./PostList";
import { useLocation, useNavigate } from "react-router-dom";
import RecommandedAuthors from "./RecommendedAuthors";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import SearchTopics from "./SearchTopics";
import useAuth from "../hooks/useAuth";


const SearchComponent = ({}) => {

  const AUTHORSEARCH_URL = "/userprofile/search";
  const axiosPrivate = useAxiosPrivate();

    const location = useLocation();
    const navigate = useNavigate();
    const [searchString, setSearchString] = useState(location.state.inputstring);
    const [authors, setAuthors] = useState([]);
    const [errMsg, setErrMsg] = useState("");
    const { auth, setAuth } = useAuth();


    useEffect(() => {
      setSearchString(location.state.inputstring);
    },[location.state.inputstring])

    useEffect(() => {
      const fetchAuthors = async () => {
        try {
          const response = await axiosPrivate.post(
            AUTHORSEARCH_URL + "?page=0&size=20",  {"searchBy": "author", "searchString": searchString}
          );
          setAuthors(response.data.authors);
        } catch (err) {
          if (!err?.response) {
            setErrMsg("No Server Response");
          } else if (err.response?.status === 400) {
            setErrMsg("Missing Username or Password");
          } else if (err.response?.status === 401) {
            setErrMsg("Unauthorized. Login again by clicking on sign-in on top right");
            setAuth(null);
          } else {
            setErrMsg("authors could not be fetched");
          }
  
        }
  
      };
      fetchAuthors();
  
    }, [searchString]);


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
            searchBy={"searchstring"}
            searchString={searchString}
            title={""}
          />
        </div>
      </div>
      <div className="right-container">
        <div className="right-top-container">
          <SearchTopics searchString={searchString}/>
        </div>
        <div className="right-bottom-container">
        <h3
          style={{
            margin: "30px 10px 30px 10px",
            fontSize: "25px",
            fontWeight: "500",
            paddingLeft: "10px"
          }}
        ><span style={{color:"grey"}}>People matching &nbsp;</span> {searchString} </h3>

        <RecommandedAuthors authors={authors}/>
      </div>
    </div>

        </div>   
  );
};
export default SearchComponent;
