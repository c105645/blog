import React from 'react';
import {FaComments, FaThumbsDown, FaThumbsUp} from "react-icons/fa";
import {Card, CardBody, CardTitle, CardText, Badge } from 'reactstrap'
import avatar from '../svg/avatar-male.svg'

import './PostTile.css'

const PostTile = (props) => {
    return (
      <Card style={{ width: '100%', marginBottom: "15px", border: "none"}}>
        <CardBody>
          <CardTitle><span style={{paddingRight:'10px'}}><img style={{borderRadius: '50%'}} src={avatar} width="25px" height="25px"/></span>{props.post.createdBy} &nbsp; . &nbsp; {props.post.createdAt}</CardTitle>
          <div className="tileGridContainer">
           <div className="tileText">
             <div><h3>{props.post.title}</h3></div>
            <div><b>{props.post.short_description.slice(0,200)}...</b></div>
           </div>
           
            <div className="tileImage"><img src={props.post.imageUrl} width="100px" height="100px"/></div>
          </div>
          <div className="cardFooter">
            <div style={{"backgroundColor": "aliceblue", "borderRadius": "10%" }}>{props.post.category}</div>
            <div><FaThumbsUp /><Badge pill outline="true" color="light" className="text-dark">{props.post.upVoteCount}</Badge> </div>
            <div><FaThumbsDown /><Badge pill outline="true" color="light" className="text-dark">{props.post.downVoteCount}</Badge> </div>
            <div><FaComments /><Badge pill outline="true" color="light" className="text-dark">{props.post.commentCount}</Badge> </div>
            <div>{props.showReason}</div>
          </div>
          <hr />
        </CardBody>
      </Card>

    );
}
export default PostTile;


