import React, {useEffect} from "react";
import AuthorLeftPanel from "./AuthorLeftPanel";
import AuthorRightPanel from "./AuthorRightPanel";
import { useLocation, useNavigate } from "react-router-dom";


const AuthorAndTopicHome = () => {
  const navigate = useNavigate();
  const location = useLocation();
  console.log(location.state.data);
  console.log(location.pathname);



  useEffect(() => {
    if(!location.state.data) {
      navigate({pathname:"/"});
    }
  }, [location]);

  return (
    <>
      <div className="AuthorAndTopicHome">
        {(location.pathname == "/author" )&& (
          <AuthorLeftPanel author={location.state.data} />
        )}
      </div>

      <div className="right_container">
        {(location.pathname == "/author" ) && (
          <AuthorRightPanel author={location.state.data} />
        )}
      </div>
    </>
  );
};
export default AuthorAndTopicHome;
