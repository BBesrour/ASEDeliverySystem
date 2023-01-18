import * as React from 'react';
import {useEffect, useState} from 'react';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import PageLayout from "../PageLayout";
import {getDeliveries} from "../../../api/delivery/deliveries";
import Delivery from "../../../model/Delivery";
import CreateDeliveryDialog from "./CreateDeliveryDialog";
import DeliveriesList from "./DeliveriesList";

export default function DispatcherDeliveriesPage() {
    const [deliveries, setDeliveries] = useState<Delivery[]>([]);
    useEffect(() => {
        getDeliveries().then(setDeliveries);
    }, []);
    const [showCreateDialog, setShowCreateDialog] = useState(false);
    const dispatcherDeliveriesProperties = ["targetCustomerID", "targetBoxID", "delivererID", "isActive"];
    const actionButtons = <>
        <Button variant="contained" onClick={() => setShowCreateDialog(true)}>Create delivery</Button>
    </>;
    return <PageLayout title="Deliveries" description={null} actionButtons={actionButtons} content={<>
        <Typography variant="h2">All Deliveries</Typography>
        <DeliveriesList deliveries={deliveries}
                        propertiesToShow={dispatcherDeliveriesProperties}
                        onDeliveryDeleted={(idToDelete) => setDeliveries(deliveries.filter((d) => d.id !== idToDelete))}
        />
        <CreateDeliveryDialog
            open={showCreateDialog}
            handleClose={() => setShowCreateDialog(false)}
            onDeliveryCreated={(delivery) => setDeliveries([delivery, ...deliveries])}
        />
    </>}/>;
}