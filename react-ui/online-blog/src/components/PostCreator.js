import React, { useEffect, useState } from "react";
import "./PostCreator.css";
import MediumEditor from "medium-editor";
import PostTile from "./PostTile";
import { Form } from "react-bootstrap";
import useAuth from "../hooks/useAuth";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { useNavigate } from "react-router";
import PostPreview from './PostPreview'

const PostCreator = () => {
    
  const [errMsg, setErrMsg] = useState("");
  const axiosPrivate = useAxiosPrivate();
  const { auth, setAuth } = useAuth();
  const [topics, setTopics] = useState();
  const navigate = useNavigate("");

  const [previewMode, setPreviewMode] = useState(false);
  const [previewButtonDisabled, setPreviewButtonDisabled] = useState(true);
  const [publishButtonDisabled, setPublishButtonDisabled] = useState(true);
  const [postObj, setPostObj] = useState({});
  const appendEditor = () => {
    setTimeout(() => {
      let editor = new MediumEditor(".editor-area", {
        placeholder: {
          text: "Tell your story...",
        },
        toolbar: {
          buttons: [
            "bold",
            "italic",
            "underline",
            "anchor",
            {
              name: "h3",
              action: "append-h3",
              aria: "header type 1",
              tagNames: ["h3"],
              contentDefault: "<b>H1</b>",
              classList: ["custom-class-h1"],
              attrs: {
                "data-custom-attr": "attr-value-h1",
              },
            },
            {
              name: "h4",
              action: "append-h4",
              aria: "header type 2",
              tagNames: ["h4"],
              contentDefault: "<b>H2</b>",
              classList: ["custom-class-h2"],
              attrs: {
                "data-custom-attr": "attr-value-h2",
              },
            },
          ],
        },
      });
      editor.subscribe("editableInput", togglePublishButton);
    }, 0);
  };

  const POSTLISTURL = "/post";
  const TOPICS_URL = "/userprofile/categoery";

  useEffect(() => {
    const fetchtopics = async () => {
      try {
        const response = await axiosPrivate.get(TOPICS_URL);

        setTopics(response.data);
      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg("Missing Username or Password");
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
    fetchtopics();
  }, []);

  const previewPost = () => {
    console.log("post preview");
    let postContent = document.querySelector(
      ".editor-container > .editor-area"
    );
    let postTitle = document.querySelector(".editor-container > h1.title");
    let post = {
      title: postTitle?.innerText,
      imageUrl: postContent
        ?.querySelector("img:first-child")
        ?.getAttribute("src"),
      short_description: postContent?.innerText,
      postDetails: {
        content: postContent?.innerHTML,
      },
      createdBy: auth.user.username,
      createdAt: Date.now(),
    };
    setPostObj(post);
    setPreviewMode(true);
  };

  const publishPost = () => {
    let postContent = document.querySelector(
      ".editor-container > .editor-area"
    );
    let postTitle = document.querySelector(".editor-container > h1.title");
    let postCategory = document.querySelector(
      ".preview-mode #post-category"
    ).value;

    console.log(postCategory);

    let post = {
      title: postTitle?.innerText,
      category: postCategory,
      imageUrl: postContent
        ?.querySelector("img:first-child")
        ?.getAttribute("src")
        ? postContent?.querySelector("img:first-child")?.getAttribute("src")
        : "",
      short_description: postContent?.innerText,
      postDetails: {
        content: postContent?.innerHTML,
      },

      comments: [],
    };

    const createPost = async () => {
      try {
        const response = await axiosPrivate.post(POSTLISTURL, post);

        setPostObj(response.data);
        navigate("/read/" + response.data.id);
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
          setErrMsg("Post could not be created");
        }
      }
    };
    createPost();
  };

  const editPost = () => {
    setPreviewMode(false);
  };

  const togglePublishButton = (ev) => {
    let postContent = document
      .querySelector(".editor-container > .editor-area")
      ?.innerText.trim();
    let postTitle = document
      .querySelector(".editor-container > h1.title")
      ?.innerText.trim();
    let previewBtn = document.querySelector(".editor-navbar > #preview-button");
    console.log("postContent :" + postContent);
    console.log("postTitle :" + postTitle);
    if (postTitle && postContent && postTitle !== "" && postContent !== "") {
      previewBtn.classList.add("btn-green-enabled");
      setPreviewButtonDisabled(false);
    } else {
      previewBtn.classList.remove("btn-green-enabled");
      setPreviewButtonDisabled(true);
    }
  };

  const titleKeyDownhandler = (ev) => {
    if (ev.nativeEvent.key === "Enter") {
      ev.preventDefault();
      document.querySelector(".editor-area").focus();
    }
  };

  const categorySelectHandler = () => {
    let publishBtn = document.querySelector(".preview-mode #publish-button");
    let postCategory = document.querySelector(
      ".preview-mode #post-category"
    ).value;
    if (postCategory === "") {
      publishBtn.classList.remove("btn-green-enabled");
      setPublishButtonDisabled(true);
    } else {
      publishBtn.classList.add("btn-green-enabled");
      setPublishButtonDisabled(false);
    }
  };
  return (
    <div className="container">
      <div className="editor-mode" hidden={previewMode}>
        <div className="editor-navbar">
          <button
            className="btn-green"
            id="preview-button"
            onClick={previewPost}
            disabled={previewButtonDisabled}
          >
            Publish
          </button>
        </div>
        <div className="editor-container" hidden={previewMode}>
          <h1
            className="box title place-holder"
            data-text="Title.."
            contentEditable
            onKeyDown={titleKeyDownhandler}
            onKeyUp={togglePublishButton}
          ></h1>
          <div className="editor-area">{appendEditor()}</div>
        </div>
      </div>
      <div className="preview-mode" hidden={!previewMode}>
        <div>
          <b style={{ fontSize: "1.3rem" }}>Post Preview</b>
          <button className="btn-green-enabled" onClick={editPost}>
            Edit
          </button>
        </div>
        <div className="tile">
          <PostPreview
            postObj={postObj}
            userObj={auth.user}
            postDetailsObj={postObj.postDetails}
          />
        </div>
        <br />
        <br />
        <div>
          <Form.Select
            aria-label="Select Category"
            id="post-category"
            onChange={categorySelectHandler}
          >
            <option value="">Select Category...</option>
            {topics?.map(({ name }) => (
              <option key={name} value={name}>{name}</option>
            ))}
          </Form.Select>
        </div>
        <div>
          <button
            className="btn-green"
            id="publish-button"
            onClick={publishPost}
            disabled={publishButtonDisabled}
          >
            Publish Now
          </button>
        </div>
      </div>
    </div>
  );
};

export default PostCreator;
