import React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import { createDelivery } from "../../../api/delivery/deliveries";
import Delivery from "../../../model/Delivery";
import DeliveryFields from "./DeliveryFields";

export default function CreateDeliveryDialog({
  open,
  handleClose,
  onDeliveryCreated,
}: {
  open: boolean;
  handleClose: () => void;
  onDeliveryCreated: (delivery: Delivery) => void;
}) {
  const [delivery, setDelivery] = React.useState(
    new Delivery(null, "", "", "", "ORDERED", true)
  );

  const handleCreateDelivery = () => {
    createDelivery(delivery)
      .then((res) => {
        onDeliveryCreated(res);
        handleClose();
      })
      .catch((err) => {
        if (err.response.status === 409) {
          alert("Box does not exist! or assigned to another Customer");
        }
      });
  };

  return open ? (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>Create New Delivery</DialogTitle>
      <DialogContent>
        <DialogContentText>Enter new delivery details here:</DialogContentText>
        <DeliveryFields delivery={delivery} onDeliveryUpdate={setDelivery} />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={handleCreateDelivery}>Create</Button>
      </DialogActions>
    </Dialog>
  ) : (
    <></>
  );
}
