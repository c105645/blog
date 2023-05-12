import React, { useState, useEffect } from "react";
import PostList from "./PostList";
import useAuth from "../hooks/useAuth";
import RecommandedTopics from "./RecommandedTopics";
import RecommandedAuthors from "./RecommendedAuthors";
import useAxiosPrivate from "../hooks/useAxiosPrivate";


import "./Home.css";

const Home = () => {
  const AUTHORS_URL = "/userprofile";
  const FOLLOWINGLISTURL = "/userprofile/authors/following"

  const axiosPrivate = useAxiosPrivate();
  const { auth, setAuth } = useAuth();
  const [authors, setAuthors] = useState([]);
  const [errMsg, setErrMsg] = useState("");
  const [currentTab, setCurrentTab] = useState("ForYou");
  const tabs = [
    {
      id: "ForYou",
      tabTitle: "For You",
      title: "Based on your reading history",
    },
    {
      id: "Following",
      tabTitle: "Following",
      title: "From your favorite authors",
    },
    ...auth.user.categories.map((cat) => {
      return {
        id: cat.id,
        tabTitle: cat.name,
        title: "Based on your interested topics",
      };
    }),
  ];


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

  const handleTabClick = (e) => {
    setCurrentTab(e.target.id);
  };

  return (
    <div className="main_container">
      <div className="left_container">
        <div className="tabs">
          {tabs.map((tab, i) => (
            <button
              key={i}
              id={tab.id}
              disabled={currentTab === `${tab.id}`}
              onClick={handleTabClick}
            >
              {tab.tabTitle}
            </button>
          ))}
        </div>
        <div className="content">
          {tabs.map((tab, i) => (
            <div key={i}>
              {currentTab === `${tab.id}` && (
                <div>
                  <PostList
                    searchBy={tab.tabTitle == "Following" ?  "following" : tab.tabTitle == "For You"? "foryou" :  "categoery" || "foryou"}
                    searchString={tab.tabTitle}
                    title={tab.title}
                    authors={authors?.map(author => author.username)}
                  />
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
      <div className="right_container">
        <RecommandedTopics />

        <h3
          style={{
            margin: "90px 10px 30px 10px",
            fontSize: "25px",
            fontWeight: "500",
          }}
        >
          Recommended Authors
        </h3>

        <RecommandedAuthors authors={authors}/>
      </div>
    </div>
  );
};
export default Home;
