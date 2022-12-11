import * as React from 'react';
import Button from '@mui/material/Button';
import {Routes, Route, useNavigate} from 'react-router-dom';
import DispatcherBoxes from './DispatcherBoxes';
import DispatcherUsers from './DispatcherUsers';
import DispatcherDeliveries from './DispatcherDeliveries';

export default function Dispatcher() {
  const navigate = useNavigate();

  const navigateToBoxes = () => {
    navigate('/DispatcherBoxes');
  };

  const navigateToUsers = () => {
    navigate('/DispatcherUsers');
  };

  const navigateToDeliveries = () => {
    navigate('/DispatcherDeliveries');
  };


  return (
    <div>
      <div>
        <Button onClick={navigateToUsers}>Users</Button>
        <Button onClick={navigateToBoxes}>Boxes</Button>
        <Button onClick={navigateToDeliveries}>Deliveries</Button>

        <Routes>
          <Route path="/DispatcherBoxes" element={<DispatcherBoxes />} />
          <Route path="/DispatcherUsers" element={<DispatcherUsers />} />
          <Route path="/DispatcherDeliveries" element={<DispatcherDeliveries />} />
        </Routes>
      </div>
    </div>
  );
}


