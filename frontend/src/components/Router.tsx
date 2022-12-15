import * as React from 'react';
import Button from '@mui/material/Button';
import {Route, Routes, useNavigate} from 'react-router-dom';
import UsersPage from './pages/UsersPage';
import BoxesPage from "./pages/boxes/BoxesPage";
import DeliveriesPage from "./pages/DeliveriesPage";

export default function Router() {
  const navigate = useNavigate();

  const navigateToBoxes = () => {
    navigate('/boxes');
  };

  const navigateToUsers = () => {
    navigate('/users');
  };

  const navigateToDeliveries = () => {
    navigate('/deliveries');
  };


  return (
    <div>
      <div>
        <Button onClick={navigateToUsers}>Users</Button>
        <Button onClick={navigateToBoxes}>Boxes</Button>
        <Button onClick={navigateToDeliveries}>Deliveries</Button>

        <Routes>
          <Route path="/boxes" element={<BoxesPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/deliveries" element={<DeliveriesPage />} />
        </Routes>
      </div>
    </div>
  );
}


