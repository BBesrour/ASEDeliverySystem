import React, {useEffect, useState} from "react";
import {getDeliveries} from "../../../api/delivery/deliveries";
import DeliveriesList from "./DeliveriesList";
import Typography from "@mui/material/Typography";
import Delivery from "../../../model/Delivery";

export default function DelivererDeliveriesPage() {
    const [deliveries, setDeliveries] = useState<Delivery[]>([]);
    useEffect(() => {
        getDeliveries().then(setDeliveries);
    }, []);
    const dispatcherDeliveriesProperties = ["targetBoxID", "targetCustomerID", "delivererID", "status", "active"];
    return <>
        <Typography variant="h2">All Deliveries</Typography>
        <DeliveriesList deliveries={deliveries}
                        propertiesToShow={dispatcherDeliveriesProperties}
                        onDeliveryUpdated={(delivery) => {
                            setDeliveries(deliveries.map(d => d.id === delivery.id ? delivery : d));
                        }}
                        onDeliveryDeleted={(id) => {
                            setDeliveries(deliveries.filter(d => d.id !== id));
                        }}
        />
    </>;
}