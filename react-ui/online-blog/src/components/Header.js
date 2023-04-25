import React, {useState} from "react";
import useAuth from "../hooks/useAuth";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';

const Header = () => {
  const { auth } = useAuth();
  const [searchInput, setSearchInput] = useState("");
  
  const handleChange = (e) => {
    e.preventDefault();
    setSearchInput(e.target.value);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setSearchInput(e.target.value);
  };

  return (
    <div>
      <h1>My Blog</h1>
      <InputGroup className="mb-3">
        <Form.Control
          type="text"
          id="searchInput"
          autoComplete="off"
          onChange={(e) => setSearchInput(e.target.value)}
          value={searchInput}
          placeholder="Search here..."
          aria-label="search"
          aria-describedby="search-box"
        />
        <Button type="button" onClick={handleSearch} >
        <FontAwesomeIcon size="xs" icon={faSearch} />
        </Button>
      </InputGroup>
      {auth?.user?.username}
    </div>
  );
};
export default Header;
