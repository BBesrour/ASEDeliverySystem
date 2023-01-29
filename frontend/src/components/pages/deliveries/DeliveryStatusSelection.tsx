import React from "react";
import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";

export default function DeliveryStatusSelection({status, onStatusUpdate} : {
    status: string,
    onStatusUpdate: (status: string) => void
}) {
    return <FormControl fullWidth>
        <InputLabel id="delivery-status-label">Delivery Status</InputLabel>
        <Select
            labelId="delivery-status-label"
            id="delivery-status"
            value={status}
            label="Delivery Status"
            onChange={evt => onStatusUpdate(evt.target.value)}
        >
            <MenuItem value="ORDERED">ordered</MenuItem>
            <MenuItem value="DELIVERED">delivered</MenuItem>
            <MenuItem value="PICKED_UP">picked up</MenuItem>
        </Select>
    </FormControl>
}