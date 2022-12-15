import React, {useEffect, useState} from "react";
import {getActiveDeliveries, getInactiveDeliveries} from "../../../api/delivery/deliveries";
import {getUserID} from "../../../storage/user";
import DeliveriesList from "./DeliveriesList";
import Typography from "@mui/material/Typography";
import PageLayout from "../PageLayout";
import Delivery from "../../../model/Delivery";

export default function CustomerDeliveriesPage() {
    const userID = getUserID();
    const [activeDeliveries, setActiveDeliveries] = useState<Delivery[]>([]);
    const [inactiveDeliveries, setInactiveDeliveries] = useState<Delivery[]>([]);
    useEffect(() => {
        getActiveDeliveries(userID).then(setActiveDeliveries);
        getInactiveDeliveries(userID).then(setInactiveDeliveries);
    }, []);
    const customerDeliveriesProperties = ["targetBoxID"];
    return <PageLayout title="Deliveries" description={null} actionButtons={<></>} content={
        <>
            <Typography variant="h2">Active Deliveries</Typography>
            <DeliveriesList deliveries={activeDeliveries} propertiesToShow={customerDeliveriesProperties} />
            <Typography variant="h2">Past Deliveries</Typography>
            <DeliveriesList deliveries={inactiveDeliveries} propertiesToShow={customerDeliveriesProperties} />
        </>
    }/>;
}