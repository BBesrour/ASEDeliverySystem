import * as React from 'react';
import Button from '@mui/material/Button';
import { Routes, Route, useNavigate } from 'react-router-dom';
import CustomerActiveDeliveries from './CustomerActiveDeliveries';
import CustomerPastDeliveries from './CustomerPastDeliveries';

export default function Customer() {
    const navigate = useNavigate();

    const navigateToActiveDeliveries = () => {
        navigate('/CustomerActiveDeliveries');
    };

    const navigateToPastDeliveries = () => {
        navigate('/CustomerPastDeliveries');
    };

    return (
        <div>
            <div>
                <Button onClick={navigateToActiveDeliveries}>Active deliveries</Button>
                <Button onClick={navigateToPastDeliveries}>Past deliveries</Button>

                <Routes>
                    <Route path="/CustomerActiveDeliveries" element={<CustomerActiveDeliveries />} />
                    <Route path="/CustomerPastDeliveries" element={<CustomerPastDeliveries />} />
                </Routes>
            </div>
        </div>
    );
}


