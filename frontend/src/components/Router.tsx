import * as React from 'react';
import Button from '@mui/material/Button';
import {Route, Routes, useNavigate} from 'react-router-dom';
import UsersPage from './pages/users/UsersPage';
import BoxesPage from "./pages/boxes/BoxesPage";
import DeliveriesPage from "./pages/deliveries/DeliveriesPage";
import {getRole} from "../storage/user";
import {ROLE_DISPATCHER} from "../model/roles";
import DeliveryStatusPage from "./pages/deliveries/DeliveryStatusPage";

export default function Router() {
    const navigate = useNavigate();
    const role = getRole();

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
                {role === ROLE_DISPATCHER && <>
                    <Button onClick={navigateToUsers}>Users</Button>
                    <Button onClick={navigateToBoxes}>Boxes</Button>
                </>}
                <Button onClick={navigateToDeliveries}>Deliveries</Button>

                <Routes>
                    <Route path="/boxes" element={<BoxesPage />} />
                    <Route path="/users" element={<UsersPage />} />
                    <Route path="/deliveries" element={<DeliveriesPage />}>
                        <Route path="change_status" element={<DeliveryStatusPage />} />
                    </Route>
                </Routes>
            </div>
        </div>
    );
}


