import React, { Fragment, useState } from "react";
import PostList from "./PostList";
import { FaPlus } from "react-icons/fa";
import useAuth from "../hooks/useAuth";

import "./Home.css";

const Home = () => {
  const { auth } = useAuth();
  const [currentTab, setCurrentTab] = useState("1");
  const tabs = [
    {
      id: "Interests",
      tabTitle: "+",
      title: "Choose your topics",
    },
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

  const handleTabClick = (e) => {
    setCurrentTab(e.target.id);
  };

  return (
    <div className="mainContainer">
      <div className="container">
        <div className="tabs">
          {tabs.map((tab, i) => (
            <button
              key={i}
              id={tab.id}
              disabled={currentTab === `${tab.id}`}
              onClick={handleTabClick}
            >
              {tab.tabTitle == "+" ? <FaPlus /> : tab.tabTitle}
            </button>
          ))}
        </div>
        <div className="content">
          {tabs.map((tab, i) => (
            <div key={i}>
              {currentTab === `${tab.id}` && (
                <div>
                  <PostList searchBy={tab.tabTitle} title={tab.title} />
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
      <div className="rightContainer">right container</div>
    </div>
  );
};
export default Home;
