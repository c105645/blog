import Register from './auth-components/Register';
import Home from './components/Home';
import Missing from './auth-components/Missing';
import RequireAuth from './auth-components/RequireAuth';
import AuthorAndTopicHome from './components/AuthorAndTopicHome';
import Layout from './auth-components/Layout';
import Login from './auth-components/Login';
import { Routes, Route } from 'react-router-dom';
import SearchComponent  from './components/SearchComponent';
import PostCreator from './components/PostCreator';
import PostReader from './components/PostReader';



function App() {

  return (
  <Routes>
    <Route path="/" element={<Layout />}>
        {/* public routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        we want to protect these routes
        <Route element={<RequireAuth allowedRoles={["User"]} />}>
          <Route path="/" element={<Home />} />
        </Route>

        <Route element={<RequireAuth allowedRoles={["User"]} />}>
          <Route path="/author" element={<AuthorAndTopicHome />} />
        </Route>

        <Route element={<RequireAuth allowedRoles={["User"]} />}>
          <Route path="/topic" element={<AuthorAndTopicHome />} />
        </Route>

        <Route element={<RequireAuth allowedRoles={["User"]} />}>
          <Route path="/search" element={<SearchComponent />} />
        </Route>

        <Route element={<RequireAuth allowedRoles={["User"]} />}>
          <Route path="/new-story" element={<PostCreator />} />
        </Route>

        <Route element={<RequireAuth allowedRoles={["User"]} />}>
          <Route path="/read/:postId" element={<PostReader />} />
        </Route>

        {/* catch all */}
        <Route path="*" element={<Missing />} />
    </Route>
  </Routes>
  );
}

export default App;