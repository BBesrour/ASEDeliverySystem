import Delivery from "../../../model/Delivery";
import React, {useEffect, useState} from "react";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import QRCodeDialog from "./QRCodeDialog";
import Button from "@mui/material/Button";
import DeliveryStatusPage from "./DeliveryStatusPage";
import {deleteDelivery} from "../../../api/delivery/deliveries";
import TextField from "@mui/material/TextField";


export default function DeliveriesList({deliveries, propertiesToShow, onDeliveryDeleted}: {
    deliveries: Delivery[],
    propertiesToShow: string[],
    onDeliveryDeleted: (id : string | null) => void
}) {
    const [showQRDialogFor, setShowQRDialogFor] = useState<string | null>(null);
    const [showStatusDialogFor, setShowStatusDialogFor] = useState<string | null>(null);
    const [trackingCodeInput, setTrackingCodeInput] = useState<string>("");
    const [shownDeliveries, setShownDeliveries] = useState<Delivery[]>(deliveries);

    function getDeliveryProperty(delivery: Delivery, property: string) {
        // @ts-ignore
        return delivery[property];
    }

    function handleDeleteDelivery(id: string | null) {
        deleteDelivery(id);
        onDeliveryDeleted(id);
    }

    function getDeliveriesByTrackingCode(deliveries: Delivery[], trackingCode: string): Delivery[] {
        if (!trackingCode) {
            return deliveries;
        }
        return deliveries.filter((delivery) => delivery.id?.startsWith(trackingCode));
    }

    useEffect(() => {
        setShownDeliveries(getDeliveriesByTrackingCode(deliveries, trackingCodeInput));
    }, [deliveries, trackingCodeInput]);

    return <>
        <TextField
            id="tracking-code"
            label="Tracking Code"
            variant="outlined"
            onChange={(event) => setTrackingCodeInput(event.target.value)}
        />
        <br />
        <br />
        <Grid container spacing={4}>
            {shownDeliveries.map(delivery => (
                <Grid item key={delivery.id} xs={12} sm={6} md={4}>
                    <Card
                        sx={{height: '100%', display: 'flex', flexDirection: 'column'}}
                    >

                        <CardContent sx={{flexGrow: 1}}>
                            <Typography gutterBottom variant="h5" component="h2">
                                Delivery
                            </Typography>
                            <Typography>
                                ID: {delivery.id}
                            </Typography>
                            {propertiesToShow.map(property => (
                                <Typography key={property}>
                                    {property}: {getDeliveryProperty(delivery, property)}
                                </Typography>
                            ))}
                            <Button size="small" onClick={() => setShowStatusDialogFor(delivery.id)}>Status</Button>
                            <Button size="small" onClick={() => setShowQRDialogFor(delivery.id)}>QR</Button>
                            <Button size="small" onClick={() => handleDeleteDelivery(delivery.id)}>Delete</Button>
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
        <QRCodeDialog open={showQRDialogFor !== null} handleClose={() => setShowQRDialogFor(null)} deliveryId={showQRDialogFor ?? ""} />
        <DeliveryStatusPage id={showStatusDialogFor} handleClose={() => setShowStatusDialogFor(null)} />
    </>;
}