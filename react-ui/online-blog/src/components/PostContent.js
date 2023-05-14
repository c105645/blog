import React, { useEffect, useState } from 'react';
import './PostContent.css';
import PostComment from './PostComment';
import { AiOutlineLike, AiOutlineComment } from "react-icons/ai";
import { AiOutlineDislike } from 'react-icons/ai';
import axios from 'axios';
import parse, { domToReact } from 'html-react-parser';
import PostPreview from './PostPreview'

const PostContent = (props) => {
   
    const addCommenttoPost = (comment) => {
        props.addCommentEvt(comment);
    }

 
    return(
        <div className="post">
            {console.log(props)}
            
            <PostPreview {...props} />
            <div className="postActions">
                <div className={props.hasVoted==1?"postActionCountActive":"postActionCount"}><AiOutlineLike size='1.8rem' onClick={props.upVoteEVT} /><div>{props.postObj.upVoteCount}</div></div>
                <div className={props.hasVoted==-1?"postActionCountActive":"postActionCount"}><AiOutlineDislike size='1.8rem' onClick={props.downVoteEVT}/><div>{props.postObj.downVoteCount}</div></div>
                <div className={props.hasCommented ? "postActionCountActive":"postActionCount"}><AiOutlineComment size='1.8rem' /><div>{props.postObj.commentCount}</div></div>
            </div>
            <PostComment  postObj={props.postObj} addcommentEVT={addCommenttoPost} refreshPost={props.refreshPost}/>
        </div>
    );
}
export default PostContent;
