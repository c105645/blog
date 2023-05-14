import { useRef, useState, useEffect } from "react";
import {
  FaCheck,
  FaTimes,
  FaInfoCircle,
} from "react-icons/fa";
import axios from "../api/axios";
import { Link } from "react-router-dom";
import "./Register.css";

const USER_REGEX = /^[A-z][A-z0-9-_]{3,25}$/;
const FIRSTNAME_REGEX = /^[A-z0-9-_]{3,25}$/;
const LASTNAME_REGEX = /^[A-z0-9-_]{1,25}$/;
const BIO_REGEX = /^.+$[\n\r]*/gm;
const PASSWORD_REGEX =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/;
const EMAIL_REGEX =
  /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
const URL_REGEX =
  /^(http(s):\/\/.)[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)$/;

const REGISTER_URL = "/userprofile/signup";

const Register = () => {
  const userRef = useRef();
  const errRef = useRef();

  const [username, setUsername] = useState("");
  const [validUsername, setValidUsername] = useState(false);
  const [usernameFocus, setUsernameFocus] = useState(false);

  const [password, setPassword] = useState("");
  const [validPassword, setValidPassword] = useState(false);
  const [passwordFocus, setPasswordFocus] = useState(false);

  const [matchPassword, setMatchPassword] = useState("");
  const [validMatch, setValidMatch] = useState(false);
  const [matchFocus, setMatchFocus] = useState(false);

  const [firstName, setFirstName] = useState("");
  const [validFirstName, setValidFirstName] = useState(false);
  const [firstNameFocus, setFirstNameFocus] = useState(false);

  const [lastName, setLastName] = useState("");
  const [validLastName, setValidLastName] = useState(false);
  const [lastNameFocus, setLastNameFocus] = useState(false);

  const [email, setEmail] = useState("");
  const [validEmail, setValidEmail] = useState(false);
  const [emailFocus, setEmailFocus] = useState(false);

  const [biography, setBiography] = useState("");
  const [validBiography, setValidBiography] = useState(false);
  const [biographyFocus, setBiographyFocus] = useState(false);

  const [imageUrl, setImageUrl] = useState("");
  const [validImageUrl, setValidImageUrl] = useState(false);
  const [imageUrlFocus, setImageUrlFocus] = useState(false);

  const [errMsg, setErrMsg] = useState("");
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    userRef.current.focus();
  }, []);

  useEffect(() => {
    setValidUsername(USER_REGEX.test(username));
  }, [username]);

  useEffect(() => {
    setValidPassword(PASSWORD_REGEX.test(password));
    setValidMatch(password === matchPassword);
  }, [password, matchPassword]);

  useEffect(() => {
    setValidFirstName(FIRSTNAME_REGEX.test(firstName));
  }, [firstName]);

  useEffect(() => {
    setValidLastName(LASTNAME_REGEX.test(lastName));
  }, [lastName]);

  useEffect(() => {
    setValidBiography(BIO_REGEX.test(biography));
  }, [biography]);

  useEffect(() => {
    setValidEmail(EMAIL_REGEX.test(email));
  }, [email]);

  useEffect(() => {
    setValidImageUrl(URL_REGEX.test(imageUrl));
  }, [imageUrl]);

  useEffect(() => {
    setErrMsg("");
  }, [
    username,
    password,
    matchPassword,
    firstName,
    lastName,
    email,
    biography,
    imageUrl,
  ]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const v1 = USER_REGEX.test(username);
    const v2 = PASSWORD_REGEX.test(password);
    const v3 = FIRSTNAME_REGEX.test(firstName);
    const v4 = LASTNAME_REGEX.test(lastName);
    const v5 = BIO_REGEX.test(biography);
    const v6 = EMAIL_REGEX.test(email);
    const v7 = URL_REGEX.test(imageUrl);
    if (!v1 || !v2 || !v3 || !v4 || !v5 || !v6 || !v7) {
      setErrMsg("Invalid Entry");
      return;
    }
    try {
      const response = await axios.post(
        REGISTER_URL,
        JSON.stringify({
          username,
          password,
          firstName,
          lastName,
          email,
          biography,
          imageUrl,
          isAdmin: false,
        }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );

      setSuccess(true);
      setUsername("");
      setPassword("");
      setMatchPassword("");
      setFirstName("");
      setLastName("");
      setBiography("");
      setEmail("");
      setImageUrl("");
    } catch (err) {
      if (!err?.response) {
        setErrMsg("No Server Response");
      } else if (err.response?.status === 409) {
        setErrMsg("Username Taken");
      } else {
        setErrMsg("Registration Failed");
      }
      errRef.current.focus();
    }
  };

  return (
    <div className="login-container">
      {success ? (
        <section>
          <h1>Registration Successful!</h1>
          <p>
          <Link to="/">Sign In</Link>
          </p>
        </section>
      ) : (
        <section className="register-section">
          <p
            ref={errRef}
            className={errMsg ? "errmsg" : "offscreen"}
            aria-live="assertive"
          >
            {errMsg}
          </p>
          <h4>Register</h4>
          <form onSubmit={handleSubmit}>
            <div className="form-container">
              <div className="form-element">
                <label  className="mt-2"  htmlFor="username">
                  Username:
                  <FaCheck
                    className={validUsername ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={validUsername || !username ? "hide" : "invalid"}
                  />
                </label>
                <input
                  type="text"
                  id="username"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setUsername(e.target.value)}
                  value={username}
                  required
                  aria-invalid={validUsername ? "false" : "true"}
                  aria-describedby="usernamenote"
                  onFocus={() => setUsernameFocus(true)}
                  onBlur={() => setUsernameFocus(false)}
                />
                <p
                  id="usernamenote"
                  className={
                    usernameFocus && username && !validUsername
                      ? "instructions"
                      : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  4 to 24 characters.
                  <br />
                  Must begin with a letter.
                  <br />
                  Letters, numbers, underscores,
                  <br /> hyphens allowed.
                </p>
              </div>

              <div className="form-element">
                <label  className="mt-2"  htmlFor="email">
                  Email:
                  <FaCheck
                    className={validEmail ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={validEmail || !email ? "hide" : "invalid"}
                  />
                </label>
                <input
                  type="text"
                  id="email"
                  autoComplete="off"
                  onChange={(e) => setEmail(e.target.value)}
                  value={email}
                  required
                  aria-invalid={validEmail ? "false" : "true"}
                  aria-describedby="emailnote"
                  onFocus={() => setEmailFocus(true)}
                  onBlur={() => setEmailFocus(false)}
                />
                <p
                  id="emailnote"
                  className={
                    emailFocus && email && !validEmail
                      ? "instructions"
                      : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  Should be a valid email address.
                </p>
              </div>

              <div className="form-element">
                <label  className="mt-2"  htmlFor="password">
                  Password:
                  <FaCheck
                    className={validPassword ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={validPassword || !password ? "hide" : "invalid"}
                  />
                </label>
                <input
                  type="password"
                  id="password"
                  onChange={(e) => setPassword(e.target.value)}
                  value={password}
                  required
                  aria-invalid={validPassword ? "false" : "true"}
                  aria-describedby="passwordnote"
                  onFocus={() => setPasswordFocus(true)}
                  onBlur={() => setPasswordFocus(false)}
                />
                <p
                  id="passwordnote"
                  className={
                    passwordFocus && !validPassword
                      ? "instructions"
                      : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  8 to 24 characters.
                  <br />
                  Must include uppercase and <br /> lowercase letters, a number{" "}
                  <br /> and a special character.
                  <br />
                  Allowed special characters: <br />
                  <span aria-label="exclamation mark">!</span>{" "}
                  <span aria-label="at symbol">@</span>{" "}
                  <span aria-label="hashtag">#</span>{" "}
                  <span aria-label="dollar sign">$</span>{" "}
                  <span aria-label="percent">%</span>
                </p>
              </div>
              <div className="form-element">
                <label  className="mt-2"  htmlFor="confirm_pwd">
                  Confirm Password:
                  <FaCheck
                    className={validMatch && matchPassword ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={
                      validMatch || !matchPassword ? "hide" : "invalid"
                    }
                  />
                </label>
                <input
                  type="password"
                  id="confirm_pwd"
                  onChange={(e) => setMatchPassword(e.target.value)}
                  value={matchPassword}
                  required
                  aria-invalid={validMatch ? "false" : "true"}
                  aria-describedby="confirmnote"
                  onFocus={() => setMatchFocus(true)}
                  onBlur={() => setMatchFocus(false)}
                />
                <p
                  id="confirmnote"
                  className={
                    matchFocus && !validMatch ? "instructions" : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  Must match the first password.
                </p>
              </div>
              <div className="form-element">
                <label  className="mt-2"  htmlFor="firstName">
                  First Name:
                  <FaCheck className={validFirstName ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={
                      validFirstName || !firstName ? "hide" : "invalid"
                    }
                  />
                </label>
                <input
                  type="text"
                  id="firstName"
                  autoComplete="off"
                  onChange={(e) => setFirstName(e.target.value)}
                  value={firstName}
                  required
                  aria-invalid={validFirstName ? "false" : "true"}
                  aria-describedby="firstnote"
                  onFocus={() => setFirstNameFocus(true)}
                  onBlur={() => setFirstNameFocus(false)}
                />
                <p
                  id="firstnote"
                  className={
                    firstNameFocus && firstName && !validFirstName
                      ? "instructions"
                      : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  4 to 24 characters.
                  <br />
                  Must begin with a letter.
                  <br />
                  Letters, numbers, underscores,
                  <br /> hyphens allowed.
                </p>
              </div>
              <div className="form-element">
                <label  className="mt-2"  htmlFor="lastName">
                  Last Name:
                  <FaCheck
                    className={validLastName ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={validLastName || !lastName ? "hide" : "invalid"}
                  />
                </label>
                <input
                  type="text"
                  id="lastName"
                  autoComplete="off"
                  onChange={(e) => setLastName(e.target.value)}
                  value={lastName}
                  required
                  aria-invalid={validLastName ? "false" : "true"}
                  aria-describedby="lastnote"
                  onFocus={() => setLastNameFocus(true)}
                  onBlur={() => setLastNameFocus(false)}
                />
                <p
                  id="lastnote"
                  className={
                    lastNameFocus && lastName && !validLastName
                      ? "instructions"
                      : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  1 to 24 characters.
                  <br />
                  Must begin with a letter.
                  <br />
                  Letters, numbers, underscores, hyphens allowed.
                </p>
              </div>

              <div className="form-element">
                <label  className="mt-2"  htmlFor="imageUrl">
                  Image imageUrl:
                  <FaCheck
                    className={validImageUrl ? "valid" : "hide"}
                  />
                  <FaTimes
                    className={validImageUrl || !imageUrl ? "hide" : "invalid"}
                  />
                </label>
                <input
                  type="text"
                  id="imageUrl"
                  autoComplete="off"
                  onChange={(e) => setImageUrl(e.target.value)}
                  value={imageUrl}
                  aria-invalid={validImageUrl ? "false" : "true"}
                  aria-describedby="urlnote"
                  onFocus={() => setImageUrlFocus(true)}
                  onBlur={() => setImageUrlFocus(false)}
                />
                <p
                  id="urlnote"
                  className={
                    imageUrlFocus && imageUrl && !validImageUrl
                      ? "instructions"
                      : "offscreen"
                  }
                >
                  <FaInfoCircle />
                  Should be a valid Url.
                </p>
              </div>
            </div>

            <div className="form-element">
              <label  className="mt-2"  htmlFor="biography">
                Biography:
                <FaCheck
                  className={validBiography ? "valid" : "hide"}
                />
                <FaTimes
                  className={validBiography || !biography ? "hide" : "invalid"}
                />
              </label>
              <textarea
                row="10"
                id="biography"
                autoComplete="off"
                onChange={(e) => setBiography(e.target.value)}
                value={biography}
                aria-invalid={validBiography ? "false" : "true"}
                aria-describedby="bionote"
                onFocus={() => setBiographyFocus(true)}
                onBlur={() => setBiographyFocus(false)}
              />
              <p
                id="bionote"
                className={
                  biographyFocus && imageUrl && !validBiography
                    ? "instructions"
                    : "offscreen"
                }
              >
                <FaInfoCircle />
                biography should be atleast 25 characters.
              </p>
            </div>
            <div className="form-element">
              <button className="mt-3"
                disabled={
                  !validUsername || !validPassword || !validMatch ? true : false
                }
              >
                Sign Up
              </button>
            </div>
          </form>
          <p className="already-registered">
            Already registered?
            <br />
            <span className="line">
              <Link to="/">Sign In</Link>
            </span>
          </p>
        </section>
      )}
    </div>
  );
};

export default Register;
