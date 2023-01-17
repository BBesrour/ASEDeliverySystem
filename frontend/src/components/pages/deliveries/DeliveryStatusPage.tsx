import {useSearchParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Delivery from "../../../model/Delivery";
import DialogTitle from "@mui/material/DialogTitle";
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import {getDelivery, updateDelivery} from "../../../api/delivery/deliveries";
import Button from "@mui/material/Button";

export default function DeliveryStatusPage({id, handleClose}: { id: string | null, handleClose: () => void | null }) {
    const [searchParams,] = useSearchParams();
    id = id || searchParams.get("id");
    const [delivery, setDelivery] = useState<Delivery | null>(null);
    useEffect(() => {
        if (id) {
            getDelivery(id).then(setDelivery);
        }
    }, [id]);

    return <Dialog open={!!id}>
        <DialogTitle>Change status</DialogTitle>
        <DialogContent>
            <p>Target Box ID: {delivery?.targetBoxID}</p>
            <p>Delivery active: {delivery?.active ? "active" : "inactive"}</p>
            <p>Delivery status: {delivery?.status ?? "no status"}</p>
            <Button onClick={() => {
                if (delivery) {
                    const orderedDelivery = {...delivery, status: "ORDERED"};
                    updateDelivery(orderedDelivery).then(() => {
                        setDelivery(Delivery.fromJson(orderedDelivery));
                    });
                }
            }}>Set status ORDERED</Button>
            <Button onClick={() => {
                if (delivery) {
                    const orderedDelivery = {...delivery, status: "PICKED_UP"};
                    updateDelivery(orderedDelivery).then(() => {
                        setDelivery(Delivery.fromJson(orderedDelivery));
                    });
                }
            }}>Set status PICKED_UP</Button>
            {handleClose && <Button onClick={handleClose}>Close</Button>}
        </DialogContent>
    </Dialog>;
}

DeliveryStatusPage.defaultProps = {id: null, handleClose: null};