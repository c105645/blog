import React, {useState, useEffect} from "react";
import AuthorLeftPanel from "./AuthorLeftPanel";
import AuthorRightPanel from "./AuthorRightPanel";
import { useLocation, useNavigate } from "react-router-dom";
import TopicLeftPanel from "./TopicLeftPanel";
import TopicRightPanel from "./TopicRightPanel";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { Alert } from "reactstrap";
import useAuth from "../hooks/useAuth";
const AuthorAndTopicHome = () => {

  const USERURL = "/userprofile";

  const navigate = useNavigate();
  const location = useLocation();
  const [errMsg, setErrMsg] = useState("");
  const axiosPrivate = useAxiosPrivate();
  const {auth,  setAuth } = useAuth();

  const author = location.pathname == "/author" ? location.state.data : null;
  const topic = location.pathname == "/topic" ? location.state.data : null;


  useEffect(() => {
    if(!location.state?.data) {
      navigate({pathname:"/"});
    }
  }, [location]);


  const HandleFollow = () => {
    const followReq = async () => {
      try {
        const following = auth?.user?.following.some(usr => usr.username == author.username) ? "unfollow" : "follow";

       const payload =  {
          "userId": auth.user.id,
          "toFollowId": author.id,
          "toUnFollowId": author.id

      }
        const response = await axiosPrivate.post(
          USERURL + `/${following}`, payload);

          const preuser = auth.user;
          following == "follow" ? preuser.following.push(author) : preuser.following.pop(author);
          setAuth({...auth, user: {...auth.user, following: [...preuser.following]}});
          localStorage.setItem("user-info", JSON.stringify(auth))

      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg("Bad Request ");
        } else if (err.response?.status === 401) {
          setErrMsg(
            "Unauthorized. Login again by clicking on sign-in on top right"
          );
          setAuth(null);
        } else {
          setErrMsg("Posts could not be fetched");
        }
      }
    };
    followReq();
  }


  const HandleFollowTopics = () => {
    const followReq = async () => {
      try {
        const following = auth?.user?.categories.some(cat => cat.id == topic.id) ? "unfollow" : "follow";

        const response = await axiosPrivate.post(
          USERURL + `/categoery/${topic.id}/${following}`);

          const preuser = auth.user;
          following == "follow" ? preuser.categories.push(topic) : preuser.categories.pop(topic);
          setAuth({...auth, user: {...auth.user, categories: [...preuser.categories]}});
          localStorage.setItem("user-info", JSON.stringify(auth))

      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg("Bad Request ");
        } else if (err.response?.status === 401) {
          setErrMsg(
            "Unauthorized. Login again by clicking on sign-in on top right"
          );
          setAuth(null);
        } else {
          setErrMsg("Posts could not be fetched");
        }
      }
    };
    followReq();

  }

  return (
    <>
      <div className="AuthorAndTopicHome">
        {(location.pathname == "/author" )&& (
          <AuthorLeftPanel author={location.state.data} followEvt = {HandleFollow} />
        )}
         {(location.pathname == "/topic" )&& (
          <TopicLeftPanel topic={location.state.data} followEvt = {HandleFollowTopics}/>
        )}
      </div>

      <div className="right_container">
        {(location.pathname == "/author" ) && (
          <AuthorRightPanel author={location.state.data} followEvt = {HandleFollow}  />
        )}
         {(location.pathname == "/topic" )&& (
          <TopicRightPanel topic={location.state.data} followEvt = {HandleFollowTopics}/>
        )}
      </div>
    </>
  );
};
export default AuthorAndTopicHome;
