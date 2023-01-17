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
    const dispatcherDeliveriesProperties = ["targetCustomerID", "targetBoxID", "delivererID", "status", "active"];
    return <>
        <Typography variant="h2">All Deliveries</Typography>
        <DeliveriesList deliveries={deliveries} propertiesToShow={dispatcherDeliveriesProperties} />
    </>;
}