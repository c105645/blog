import React, {useState, useEffect} from 'react';
import './RecommandedTopics.css'
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import useAuth from "../hooks/useAuth";
import { useNavigate } from "react-router-dom"



const RecommandedTopics = () => {

const TOPICS_URL = "/userprofile/categoery";
const [topics, setTopics] = useState();
const [errMsg, setErrMsg] = useState('');
const axiosPrivate = useAxiosPrivate();
const { auth, setAuth } = useAuth();
const navigate = useNavigate();


useEffect(() => {
  const fetchtopics = async () => {
    try {
      const response = await axiosPrivate.get(
        TOPICS_URL + "?page=0&size=20",
      );
      console.log(response.data);
      console.log(auth.user.categories);
      setTopics(response.data.map(topic => ({...topic, isFollowed: auth.user.categories.some(({id}) => topic.id === id)})));
      console.log(topics);
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
  fetchtopics();

}, [])
  return (
      <div className="recommended-topics">
      <h2>Recommended topics</h2>
      <div className="topic">
     { topics?.map((cat) => (<button key={cat.id} style={{backgroundColor: cat.isFollowed ? 'blueviolet' : 'cornflowerblue'}}
             onClick={() => navigate("/topic", {state: {type: "categoery", data: cat}})}
             >{cat.name}</button>))}
      </div>
</div>)
}
export default RecommandedTopics;
