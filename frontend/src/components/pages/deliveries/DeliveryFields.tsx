import React, {useEffect, useState} from "react";
import Delivery from "../../../model/Delivery";
import TextField from "@mui/material/TextField";
import Box from "../../../model/Box";
import BoxSelection from "../helpers/BoxSelection";
import DeliveryStatusSelection from "./DeliveryStatusSelection";
import {FormControlLabel, FormGroup, Switch} from "@mui/material";

export default function DeliveryFields({delivery, onDeliveryUpdate}: {
    delivery: Delivery,
    onDeliveryUpdate: (delivery: Delivery) => void
}) {
    const [targetCustomerID, setTargetCustomerID] = useState(delivery.targetCustomerID);
    const [targetBoxID, setTargetBoxID] = useState(delivery.targetBoxID);
    const [delivererID, setDelivererID] = useState(delivery.delivererID);
    const [status, setStatus] = useState(delivery.status || "ORDERED");
    const [isActive, setIsActive] = useState(delivery.isActive);

    function handleUpdateDelivery() {
        onDeliveryUpdate(new Delivery(
            delivery.id,
            targetCustomerID,
            targetBoxID,
            delivererID,
            status,
            isActive
        ));
    }

    useEffect(() => {
        handleUpdateDelivery();
    }, [targetCustomerID, targetBoxID, delivererID, status, isActive]);

    return (
        <div>
            <TextField
                autoFocus
                margin="dense"
                defaultValue={delivery.targetCustomerID}
                onChange={(event) => setTargetCustomerID(event.target.value)}
                label="Target Customer ID"
                type="text"
                fullWidth
                variant="standard"
            />
            <BoxSelection label="Target Box" onSelect={(box: Box | null) => setTargetBoxID(box?.id || "")}/>
            <TextField
                margin="dense"
                defaultValue={delivery.delivererID}
                onChange={(event) => setDelivererID(event.target.value)}
                label="Deliverer ID"
                type="text"
                fullWidth
                variant="standard"
            />
            <DeliveryStatusSelection status={status} onStatusUpdate={setStatus}/>
            <FormGroup>
                <FormControlLabel control={
                    <Switch checked={isActive}
                            onChange={(event) => setIsActive(event.target.checked)}
                            inputProps={{ 'aria-label': 'controlled' }}
                    />
                } label="Delivery Active" />
            </FormGroup>
        </div>
    );
}