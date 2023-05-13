import React, { useState, useEffect } from "react";
import "./RecommandedTopics.css";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import useAuth from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";

const SearchTopics = ({ searchString }) => {
  console.log(searchString);

  const TOPICS_URL = "/userprofile/search";
  const [topics, setTopics] = useState([]);
  const [errMsg, setErrMsg] = useState("");
  const axiosPrivate = useAxiosPrivate();
  const { auth, setAuth } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchtopics = async () => {
      try {
        const response = await axiosPrivate.post(
          TOPICS_URL + "?page=0&size=20",
          { searchBy: "categoery", searchString: searchString }
        );
        setTopics(
            response.data.categories.map((topic) => ({
            ...topic,
            isFollowed: auth.user.categories.some(({ id }) => topic.id === id),
          }))
        );
      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg("Missing Username or Password");
        } else if (err.response?.status === 401) {
          setErrMsg(
            "Unauthorized. Login again by clicking on sign-in on top right"
          );
          setAuth(null);
        } else {
          setErrMsg("topics could not be fetched");
        }
      }
    };
    fetchtopics();
  }, [searchString]);

  return (
    <div className="recommended-topics">
      <h3
        style={{
          margin: "30px 10px",
          fontSize: "25px",
          fontWeight: "500",
        }}
      >
        <span style={{ color: "grey" }}>Topics matching &nbsp;</span>{" "}
        {searchString}{" "}
      </h3>
      <div className="topic">
        {topics?.map((cat) => (
          <button
            key={cat.id}
            style={{
              backgroundColor: cat.isFollowed ? "blueviolet" : "cornflowerblue",
            }}
            onClick={() =>
              navigate("/topic", { state: { type: "categoery", data: cat } })
            }
          >
            {cat.name}
          </button>
        ))}
      </div>
    </div>
  );
};
export default SearchTopics;
