import React, { useState } from "react";
import useAuth from "../hooks/useAuth";
import { FaSearch, FaSignOutAlt, FaEdit, FaSignInAlt } from "react-icons/fa";
import { BsPersonCircle } from "react-icons/bs";
import {
  Button,
  InputGroup,
  Nav,
  Navbar,
  NavbarBrand,
  NavItem,
} from "reactstrap";
import { useNavigate } from "react-router-dom";

import "./Header.css";
import logo from "../svg/logo.svg";

import { Link, useLocation } from "react-router-dom";

const Header = () => {
  const { auth, setAuth } = useAuth();
  const [searchInput, setSearchInput] = useState("");
  const navigate = useNavigate();

  const location = useLocation();

  const handleLogOut = () => {
    console.log("handleLogOut");
    setAuth(null);
    localStorage.setItem("user-info", null);
    navigate("/login");
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setSearchInput(e.target.value);
    if(searchInput && searchInput.length > 0)
    navigate("/search", {state:{inputstring: searchInput}});
    setSearchInput("");

  };

  return (
    (!auth?.user && (
      <>
        <div className="header-tile">
          <div className="branddetail">
            <img src={logo} className="App-logo" alt="logo" />
            <span>
              <h3>teCh haCk</h3>
            </span>
          </div>
          {location.pathname == "/login" ? (
            <span className="link">
              <Link to="/register">Sign Up</Link>
            </span>
          ) : (
            <span className="link">
              <Link to="/login">Sign In</Link>
            </span>
          )}
        </div>
      </>
    )) ||
    (auth?.user && (
      <>
        <Navbar expand="lg" sticky="top" className="header-tile">
          <Nav className="ml-auto" navbar>
            <NavbarBrand>
              <Link to="/">
                <span>
                  <img src={logo} className="App-logo" alt="logo" />
                </span>
              </Link>
            </NavbarBrand>
            <NavItem>
              <InputGroup className="navbar-inputgroup">
                <input
                  type="text"
                  id="searchInput"
                  autoComplete="off"
                  onChange={(e) => setSearchInput(e.target.value)}
                  value={searchInput}
                  placeholder="Search here..."
                  aria-label="search"
                  aria-describedby="search-box"
                  className="searchInput"
                />
                <Button onClick={handleSearch} color="secondary" className="searchButton">
                  <FaSearch />
                </Button>
              </InputGroup>
            </NavItem>
          </Nav>
          <Nav className="mr-auto" navbar>
            <NavItem className="mt-1">
              <Button outline style={{ borderStyle: "none", outline: "none" }} onClick={()=>{navigate('/new-story')}}>
                <FaEdit />
              </Button>
            </NavItem>
            <NavItem style={{ marginTop: "14px" }}>
              <p>
                <span>
                  &nbsp;&nbsp;
                  <BsPersonCircle />
                </span>
                &nbsp;{auth.user.username}&nbsp;&nbsp;
              </p>
            </NavItem>
            <NavItem className="mt-1">
              {auth?.user && (
                <Button
                  outline
                  style={{ borderStyle: "none", outline: "none" }}
                  onClick={handleLogOut}
                >
                  <FaSignOutAlt />
                </Button>
              )}

              {!auth?.user && (
                <Button
                  outline
                  style={{ borderStyle: "none", outline: "none" }}
                >
                  <FaSignInAlt />
                </Button>
              )}
            </NavItem>
          </Nav>
        </Navbar>
      </>
    ))
  );
};
export default Header;
