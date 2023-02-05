import React, { useEffect, useState } from "react";
import Delivery from "../../../model/Delivery";
import Box from "../../../model/Box";
import BoxSelection from "../helpers/BoxSelection";
import DeliveryStatusSelection from "./DeliveryStatusSelection";
import UserSelection from "../helpers/UserSelection";
import User from "../../../model/User";

export default function DeliveryFields({
  delivery,
  onDeliveryUpdate,
}: {
  delivery: Delivery;
  onDeliveryUpdate: (delivery: Delivery) => void;
}) {
  const [targetCustomerID, setTargetCustomerID] = useState(
    delivery.targetCustomerID
  );
  const [targetBoxID, setTargetBoxID] = useState(delivery.targetBoxID);
  const [delivererID, setDelivererID] = useState(delivery.delivererID);
  const [status, setStatus] = useState(delivery.status || "ORDERED");

  function handleUpdateDelivery() {
    onDeliveryUpdate(
      new Delivery(
        delivery.id,
        targetCustomerID,
        targetBoxID,
        delivererID,
        status,
        delivery.active
      )
    );
  }

  useEffect(() => {
    handleUpdateDelivery();
  }, [targetCustomerID, targetBoxID, delivererID, status, delivery.active]);

  return (
    <div>
      <UserSelection
        userId={targetCustomerID}
        label="Customer"
        onSelect={(user: User | null) => setTargetCustomerID(user?.id || "")}
        userRole={"ROLE_CUSTOMER"}
      />
      <BoxSelection
        boxID={targetBoxID}
        label="Target Box"
        onSelect={(box: Box | null) => setTargetBoxID(box?.id || "")}
      />
      <UserSelection
        userId={delivererID}
        label="Deliverer"
        onSelect={(user: User | null) => setDelivererID(user?.id || "")}
        userRole={"ROLE_DELIVERER"}
      />
      <DeliveryStatusSelection status={status} onStatusUpdate={setStatus} />
    </div>
  );
}
