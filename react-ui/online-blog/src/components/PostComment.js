import React, { useState } from 'react';
import './PostComment.css';
import { useEffect } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import ReactTimeAgo from 'react-time-ago';
import { useRef } from 'react';
import axios from 'axios';

const PostComment = (props) => {

    const [comments, setComments] = useState(props.comments);    
    const [commentInput, setCommentInput] = useState("");
    const commentInputRef = useRef("");

    useEffect(() => {
        setComments(props.postObj.comments);
    }, props.postObj.comments)

    const buttonClickHandler = () => {
        console.log("Comment Created");
        let newPost = props.postObj;
        let newComment = {
            id: newPost.comments.length+1,
            review: commentInput,
            createdBy: sessionStorage.getItem("loggedInUser"),
            updatedAt: Date.now()
        }
        commentInputRef.current.value = "";
        newPost.comments.push(newComment);
        let commentURL = "http://localhost:3001/post/"+props.postObj.id;
            axios.put(commentURL, newPost)
                .then(
                    (res)=> {
                        console.log(res.data);
                    }
                )
                .catch(
                    (err)=>console.log(err)
                );
        props.refreshPost();
    }

    const commentInpuChangeHandler = ()=>{
        setCommentInput(commentInputRef.current.value);
    }

    return(
        <div>
                <div className="commentForm">
                    <input className="commentInput" type = "text" placeholder="Leave a comment..." ref={commentInputRef} onChange={commentInpuChangeHandler}/>
                    <button className="commentBtn" disabled={commentInput==""?true:false} id="submitCommentBtn" onClick={buttonClickHandler}>Comment</button>
                </div>
            {
            props.showComments===true && comments && comments.length!=0 ?
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
