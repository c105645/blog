import React from 'react';
import {FaComments, FaThumbsDown, FaThumbsUp} from "react-icons/fa";
import {Card, CardBody, CardTitle, CardText, Badge } from 'reactstrap'
import avatar from '../svg/avatar-male.svg'

import './PostTile.css'

const PostTile = (props) => {
    return (
      <Card style={{ width: '100%', marginBottom: "15px", border: "none", cursor: "pointer"}}>
        <CardBody>
          <CardTitle style={{"fontSize": "15px", "fontWeight": "400" }}><span style={{paddingRight:'10px'}}><img style={{borderRadius: '50%'}} src={avatar} width="35px" height="35px"/></span>{props.post.createdBy} &nbsp; . &nbsp; {props.post.createdAt}</CardTitle>
          <div className="tileGridContainer">
           <div className="tileText">
             <div ><h3 style={{"fontSize": "22px", "fontWeight": "500" }}>{props.post.title}</h3></div>
            <div>{props.post?.short_description?.slice(0,200)}...</div>
           </div>
           {props.post.imageUrl ? <div className="tileImage"><img src={props.post.imageUrl} width="100px" height="100px"/></div> : null }
          </div>
          <div className="cardFooter">
            <div style={{"backgroundColor": "aliceblue", "borderRadius": "10%", "fontSize": "18px", "fontWeight": "400" }}>{props.post.category}</div>
            <div style={{"fontSize": "15px", "fontWeight": "400" }}><FaThumbsUp /><Badge pill outline="true" color="light" className="text-dark">{props.post.upVoteCount}</Badge> </div>
            <div style={{"fontSize": "15px", "fontWeight": "400" }}><FaThumbsDown /><Badge pill outline="true" color="light" className="text-dark">{props.post.downVoteCount}</Badge> </div>
            <div style={{"fontSize": "15px", "fontWeight": "400" }}><FaComments /><Badge pill outline="true" color="light" className="text-dark">{props.post.commentCount}</Badge> </div>
            <div style={{"fontSize": "15px", "fontWeight": "400" }}>{props.showReason}</div>
          </div>
          <hr />
        </CardBody>
      </Card>

    );
}
export default PostTile;


