import React, { useEffect, useState } from 'react';
import './PostContent.css';
import PostComment from './PostComment';
import { AiOutlineLike, AiOutlineComment } from "react-icons/ai";
import { AiOutlineDislike } from 'react-icons/ai';
import axios from 'axios';
import parse, { domToReact } from 'html-react-parser';

const PostContent = (props) => {
    const [showComments, setShowComments] = useState(false);
    const [upVoteCount, setUpVoteCount] = useState(0);
    const [downVoteCount, setDownVoteCount] = useState(0);
    const [upvoted, setUpVoted] = useState(false);
    const [downvoted, setDownVoted] = useState(false);

    useEffect(()=> {
        if(props.postObj.upVotedBy && props.postObj.downVotedBy) {

            setUpVoteCount(props.postObj.upVotedBy.length);
            setDownVoteCount(props.postObj.downVotedBy.length);
            isAlreadyUpvoted()?setUpVoted(true):setUpVoted(false);
            isAlreadyDownVoted()?setDownVoted(true):setDownVoted(false);
        }
    }, [props]);

    const isAlreadyUpvoted = () => {
        return props.postObj.upVotedBy && props.postObj.upVotedBy.some(user => user === sessionStorage.getItem("loggedInUser"));
    }
    
    const isAlreadyDownVoted = () => {
        return props.postObj.downVotedBy && props.postObj.downVotedBy.some(user => user === sessionStorage.getItem("loggedInUser"));
    }

    const showCommentsHandler = () => {
        setShowComments(showComments => !showComments);
    }
    
    const incrementCount = (setCount, currentCount, setVoted, voted) => {
        if(!voted) {
            setCount(currentCount+1);
            const username = sessionStorage.getItem("loggedInUser");
            setVoted(true);
            
        }
    }

    const upvote = () => {
        if(!upvoted) {
            setUpVoteCount(upVoteCount+1);
            setUpVoted(true);
            const username = sessionStorage.getItem("loggedInUser");
            let newPostObj = props.postObj;
            let upvotedArr = newPostObj.upVotedBy;
            upvotedArr.push(username);
            newPostObj['upVotedBy'] = upvotedArr;
            let upvoteURL = "http://localhost:3001/post/"+props.postObj.id;
            axios.put(upvoteURL, newPostObj)
                .then(
                    (res)=> {
                        console.log(res.data);
                    }
                )
                .catch(
                    (err)=>console.log(err)
                );
        }
    }

    const downvote = () => {
        if(!downvoted) {
            setDownVoteCount(downVoteCount+1);
            setDownVoted(true);
            const username = sessionStorage.getItem("loggedInUser");
            let newPostObj = props.postObj;
            let downvotedArr = newPostObj.downVotedBy;
            downvotedArr.push(username);
            newPostObj['downVotedBy'] = downvotedArr;
            let downvoteURL = "http://localhost:3001/post/"+props.postObj.id;
            axios.put(downvoteURL, newPostObj)
                .then(
                    (res)=> {
                        console.log(res.data);
                    }
                )
                .catch(
                    (err)=>console.log(err)
                );
        }
    }
    return(
        <div className="post">
            {console.log(props.postDetailsObj)}
            <div className="postHeader"><img className="profilePhoto" src={props.userObj.imageUrl}></img><div><div style={{fontSize:'1.4rem'}}>{props.userObj.firstName + " " + props.userObj.lastName}</div><div style={{fontSize:'1rem', color:'#7d848b'}}>{props.postObj.updatedAt}</div></div></div>
            <div className="postContent">
                <div style={{fontSize:"2.2rem", fontWeight:"bold", lineHeight:"2.5rem", paddingBottom: "1rem"}}>{props.postObj.title}</div>
                { props.postDetailsObj?props.postDetailsObj.content?parse(props.postDetailsObj.content):null:null}            </div>
            <div className="postActions">
                <div className={upvoted?"postActionCountActive":"postActionCount"}><AiOutlineLike size='1.8rem' onClick={() => {upvote()}} /><div>{upVoteCount}</div></div>
                <div className={downvoted?"postActionCountActive":"postActionCount"}><AiOutlineDislike size='1.8rem' onClick={() => {downvote()}} /><div>{downVoteCount}</div></div>
                <div className="postActionCount"><AiOutlineComment size='1.8rem' onClick={showCommentsHandler} /><div>{props.postObj.commentCount}</div></div>
            </div>
            <PostComment showComments = {showComments} postObj={props.postObj} refreshPost={props.refreshPost}/>
        </div>
    );
}
export default PostContent;
