import { Outlet } from "react-router-dom";
import Header from "../components/Header";
import './Layout.css'

const Layout = () => {
  return (
    <>
      <Header />
      <main className="App">
        <Outlet />
      </main>
    </>
  );
};

export default Layout;
