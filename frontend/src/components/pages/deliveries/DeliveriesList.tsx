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
import UpdateDeliveryDialog from "./UpdateDeliveryDialog";
import {getBoxes} from "../../../api/delivery/box";
import Box from "../../../model/Box";


export default function DeliveriesList({deliveries, propertiesToShow, onDeliveryUpdated, onDeliveryDeleted}: {
    deliveries: Delivery[],
    propertiesToShow: string[],
    onDeliveryUpdated: (delivery: Delivery) => void,
    onDeliveryDeleted: (id: string | null) => void
}) {
    const [showQRDialogFor, setShowQRDialogFor] = useState<string | null>(null);
    const [showStatusDialogFor, setShowStatusDialogFor] = useState<string | null>(null);
    const [trackingCodeInput, setTrackingCodeInput] = useState<string>("");
    const [shownDeliveries, setShownDeliveries] = useState<Delivery[]>(deliveries);
    const [showUpdateDialogFor, setShowUpdateDialogFor] = useState<string | null>(null);

    function getDeliveryProperty(delivery: Delivery, property: string) {
        // @ts-ignore
        return delivery[property];
    }

    async function handleDeleteDelivery(id: string | null) {
        await deleteDelivery(id);
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


    const [boxNames, setBoxNames] = useState<Map<string, string>>(new Map());
    const [boxes, setBoxes] = useState<Box[]>([]);
    useEffect(() => {
        getBoxes().then((boxes) => {
            setBoxes(boxes);
        });
    }, []);
    shownDeliveries.forEach((delivery) => {
        const newBoxNames = boxNames;
        newBoxNames.set(delivery.id ?? "", boxes.filter((box) => box.id === delivery.targetBoxID)[0]?.name || "no box");
        setBoxNames(newBoxNames);
    });

    return <>
        <TextField
            id="tracking-code"
            label="Tracking Code"
            variant="outlined"
            onChange={(event) => setTrackingCodeInput(event.target.value)}
        />
        <br/>
        <br/>
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
                            <Typography>
                                targetBoxName: {boxNames.get(delivery.id ?? "")}
                            </Typography>

                            {propertiesToShow.map(property => (
                                <Typography key={property}>
                                    {property}: {getDeliveryProperty(delivery, property)}
                                </Typography>
                            ))}
                            <Button size="small" onClick={() => setShowStatusDialogFor(delivery.id)}>Status</Button>
                            <Button size="small" onClick={() => setShowQRDialogFor(delivery.id)}>QR</Button>
                            <Button size="small" onClick={() => setShowUpdateDialogFor(delivery.id)}>Edit</Button>
                            <Button size="small" onClick={() => handleDeleteDelivery(delivery.id)}>Delete</Button>
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
        <QRCodeDialog open={showQRDialogFor !== null} handleClose={() => setShowQRDialogFor(null)}
                      deliveryId={showQRDialogFor ?? ""}/>
        <DeliveryStatusPage id={showStatusDialogFor} handleClose={() => setShowStatusDialogFor(null)}/>
        <UpdateDeliveryDialog
            open={!!showUpdateDialogFor}
            handleClose={() => setShowUpdateDialogFor(null)}
            onDeliveryUpdated={(delivery) => {
                const index = deliveries.findIndex(d => d.id === delivery.id);
                deliveries[index] = delivery;
                onDeliveryUpdated(delivery);
                setShowUpdateDialogFor(null)
            }}
            delivery={deliveries.find(d => d.id === showUpdateDialogFor) || new Delivery(null, "", "", "", "", false)}
        />
    </>;
}