import React, { useState } from 'react';
import './PostComment.css';
import { useEffect } from 'react';
import ReactTimeAgo from 'react-time-ago';
import TimeAgo from 'javascript-time-ago'
import en from 'javascript-time-ago/locale/en.json'


import { useRef } from 'react';
import useAxiosPrivate from "../hooks/useAxiosPrivate";

const PostComment = (props) => {

    const [comments, setComments] = useState(props.comments);    
    const [commentInput, setCommentInput] = useState("");
    const commentInputRef = useRef("");
    const axiosPrivate = useAxiosPrivate();
    TimeAgo.addDefaultLocale(en);

    const POSTLISTURL = "/post/";


    useEffect(() => {
        setComments(props.postObj.comments);
    },[props.postObj.comments])

    const buttonClickHandler = () => {
        console.log("Comment Created");
        let newComment = {
            review: commentInput,
        }
        props.addcommentEVT(newComment);
        commentInputRef.current.value = "";
 
        props.refreshPost();
    }

    const commentInpuChangeHandler = ()=>{
        setCommentInput(commentInputRef.current.value);
    }

    return(
        <div>
                <div className="commentForm">
                    <input type = "textarea" rows="10" className="commentInput"  placeholder="Leave a comment..." ref={commentInputRef} onChange={commentInpuChangeHandler}/>
                    <button className="commentBtn" disabled={commentInput==""?true:false} id="submitCommentBtn" onClick={buttonClickHandler}>Post</button>
                </div>
            {
            comments && comments.length!=0 ?
            comments.map( comment=> 
                <div className="commentBlock">
                    <div className="commentHeader">
                        <div><img src="https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1aQyud.img?w=534&h=300&m=6&x=426&y=195&s=271&d=271" /></div><div className="commentAuthor">{comment.createdBy}</div><b>.</b><div className="commentTime"><ReactTimeAgo date={comment.updatedAt} locale="en-US"/></div>
                    </div>
                    <div className="commentContent">{comment.review}</div>
                </div>
            ):null
            }
        </div>
    );
}
export default PostComment;
